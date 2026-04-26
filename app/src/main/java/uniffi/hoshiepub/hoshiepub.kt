package uniffi.hoshiepub

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node

// Replicate the UniFFI generated records exactly.
data class ManifestItem(
    val id: String,
    val href: String,
    val mediaType: String,
    val properties: String?
)

data class SpineItem(
    val idref: String,
    val linear: Boolean
)

data class TocNode(
    val label: String,
    val href: String?,
    val children: List<TocNode>
)

open class EpubError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Io(msg: String) : EpubError("IO error: $msg")
    class Parse(msg: String) : EpubError("Parse error: $msg")
    class SpineIndexOutOfRange(index: Int) : EpubError("Spine index $index out of range")
    class ManifestItemNotFound(idref: String) : EpubError("Manifest item not found for idref: $idref")
}

class EpubBook(
    private val rootDir: File,
    private val contentDir: File,
    private val bookTitle: String?,
    private val bookCoverHref: String?,
    private val manifestList: List<ManifestItem>,
    private val spineList: List<SpineItem>,
    private val tocRoot: TocNode,
    private val bookLanguage: String? = null
) {
    fun rootDir(): String = rootDir.absolutePath

    fun contentDir(): String = contentDir.absolutePath

    fun title(): String? = bookTitle

    fun coverHref(): String? = bookCoverHref

    fun manifest(): List<ManifestItem> = manifestList

    fun spine(): List<SpineItem> = spineList

    fun toc(): TocNode = tocRoot

    fun language(): String? = bookLanguage

    fun chapterAbsolutePath(spineIndex: Int): String? {
        val spineEntry = spineList.getOrNull(spineIndex) ?: throw EpubError.SpineIndexOutOfRange(spineIndex)
        val manifestEntry = manifestList.firstOrNull { it.id == spineEntry.idref } ?: return null
        return File(contentDir, manifestEntry.href).absolutePath
    }

    fun readSpineItemText(spineIndex: UInt): String {
        val idx = spineIndex.toInt()
        val spineEntry = spineList.getOrNull(idx) ?: throw EpubError.SpineIndexOutOfRange(idx)
        val idref = spineEntry.idref
        val manifestEntry = manifestList.firstOrNull { it.id == idref }
            ?: throw EpubError.ManifestItemNotFound(idref)
        
        val file = File(contentDir, manifestEntry.href)
        return try {
            file.readText(Charsets.UTF_8)
        } catch (e: Exception) {
            throw EpubError.Io("${file.absolutePath}: ${e.message}")
        }
    }

    fun destroy() {
        // No-op for Kotlin implementation
    }
}

fun parseExtractedEpub(rootDirPath: String): EpubBook {
    val root = File(rootDirPath)
    if (!root.isDirectory) {
        throw EpubError.Parse("Root directory does not exist: $rootDirPath")
    }

    try {
        val dbf = DocumentBuilderFactory.newInstance().apply {
            isNamespaceAware = true
            isCoalescing = true
        }
        val db = dbf.newDocumentBuilder()

        // 1. Parse container.xml
        val containerFile = File(root, "META-INF/container.xml")
        if (!containerFile.isFile) {
            throw EpubError.Parse("META-INF/container.xml not found")
        }
        val containerDoc = db.parse(containerFile)
        val rootFiles = containerDoc.getElementsByTagNameNS("*", "rootfile")
        if (rootFiles.length == 0) {
            throw EpubError.Parse("No rootfile element found in container.xml")
        }
        val opfPath = rootFiles.item(0).attributes.getNamedItem("full-path")?.nodeValue
            ?: throw EpubError.Parse("rootfile element has no full-path attribute")

        val opfFile = File(root, opfPath)
        if (!opfFile.isFile) {
            throw EpubError.Parse("OPF file not found: $opfPath")
        }

        val contentDir = opfFile.parentFile ?: root

        // 2. Parse OPF file
        val opfDoc = db.parse(opfFile)

        // Title & Language
        val titles = opfDoc.getElementsByTagNameNS("*", "title")
        val title = if (titles.length > 0) titles.item(0).textContent else null

        val languages = opfDoc.getElementsByTagNameNS("*", "language")
        val language = if (languages.length > 0) languages.item(0).textContent else null

        // Manifest
        val manifestItemsList = mutableListOf<ManifestItem>()
        val manifestNodes = opfDoc.getElementsByTagNameNS("*", "item")
        for (i in 0 until manifestNodes.length) {
            val node = manifestNodes.item(i) as Element
            val id = node.getAttribute("id")
            val href = node.getAttribute("href")
            val mediaType = node.getAttribute("media-type")
            val properties = node.getAttribute("properties").takeIf { it.isNotEmpty() }
            if (id.isNotEmpty() && href.isNotEmpty() && mediaType.isNotEmpty()) {
                manifestItemsList.add(ManifestItem(id, href, mediaType, properties))
            }
        }

        // Cover href
        var coverHref: String? = null
        // Method A: look for meta cover
        val metas = opfDoc.getElementsByTagNameNS("*", "meta")
        var coverId: String? = null
        for (i in 0 until metas.length) {
            val meta = metas.item(i) as Element
            if (meta.getAttribute("name") == "cover") {
                coverId = meta.getAttribute("content")
                break
            }
        }
        if (coverId != null) {
            coverHref = manifestItemsList.firstOrNull { it.id == coverId }?.href
        }
        // Method B: look for manifest item with property cover-image
        if (coverHref == null) {
            coverHref = manifestItemsList.firstOrNull { it.properties?.contains("cover-image") == true }?.href
        }

        // Spine
        val spineItemsList = mutableListOf<SpineItem>()
        val spineElement = opfDoc.getElementsByTagNameNS("*", "spine").item(0) as? Element
        val ncxId = spineElement?.getAttribute("toc")
        val itemrefs = opfDoc.getElementsByTagNameNS("*", "itemref")
        for (i in 0 until itemrefs.length) {
            val node = itemrefs.item(i) as Element
            val idref = node.getAttribute("idref")
            val linearAttr = node.getAttribute("linear")
            val linear = linearAttr.isEmpty() || linearAttr.equals("yes", ignoreCase = true)
            if (idref.isNotEmpty()) {
                spineItemsList.add(SpineItem(idref, linear))
            }
        }

        // 3. Parse Table of Contents (TOC)
        val tocChildren = mutableListOf<TocNode>()

        // Try EPUB 3 Nav first (manifest item with property "nav")
        val navItem = manifestItemsList.firstOrNull { it.properties?.contains("nav") == true }
        if (navItem != null) {
            try {
                val navFile = File(contentDir, navItem.href)
                if (navFile.isFile) {
                    val navDoc = db.parse(navFile)
                    val navs = navDoc.getElementsByTagNameNS("*", "nav")
                    var tocNav: Element? = null
                    for (i in 0 until navs.length) {
                        val nav = navs.item(i) as Element
                        val type = nav.getAttributeNS("http://www.idpf.org/2007/ops", "type")
                        if (type == "toc" || nav.getAttribute("epub:type") == "toc") {
                            tocNav = nav
                            break
                        }
                    }
                    if (tocNav == null && navs.length > 0) {
                        tocNav = navs.item(0) as Element
                    }
                    if (tocNav != null) {
                        val olElements = tocNav.getElementsByTagNameNS("*", "ol")
                        if (olElements.length > 0) {
                            val rootOl = olElements.item(0) as Element
                            tocChildren.addAll(parseNavOl(rootOl))
                        }
                    }
                }
            } catch (e: Exception) {
                // Fallback to NCX if Nav parsing fails
            }
        }

        // Try EPUB 2 NCX if EPUB 3 Nav yielded nothing or wasn't found
        if (tocChildren.isEmpty()) {
            val ncxItem = manifestItemsList.firstOrNull { it.id == ncxId }
                ?: manifestItemsList.firstOrNull { it.mediaType == "application/x-dtbncx+xml" }
            if (ncxItem != null) {
                try {
                    val ncxFile = File(contentDir, ncxItem.href)
                    if (ncxFile.isFile) {
                        val ncxDoc = db.parse(ncxFile)
                        val navMapList = ncxDoc.getElementsByTagNameNS("*", "navMap")
                        if (navMapList.length > 0) {
                            val navMap = navMapList.item(0) as Element
                            val childNodes = navMap.childNodes
                            for (i in 0 until childNodes.length) {
                                val child = childNodes.item(i)
                                if (child.nodeType == Node.ELEMENT_NODE && child.localName == "navPoint") {
                                    tocChildren.add(parseNcxNavPoint(child as Element))
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Ignore, TOC will remain empty
                }
            }
        }

        val tocRoot = TocNode("", null, tocChildren)

        return EpubBook(
            rootDir = root,
            contentDir = contentDir,
            bookTitle = title,
            bookCoverHref = coverHref,
            manifestList = manifestItemsList,
            spineList = spineItemsList,
            tocRoot = tocRoot,
            bookLanguage = language
        )

    } catch (e: EpubError) {
        throw e
    } catch (e: Exception) {
        throw EpubError.Parse("Failed to parse EPUB: ${e.message}")
    }
}

private fun parseNavOl(ol: Element): List<TocNode> {
    val list = mutableListOf<TocNode>()
    val childNodes = ol.childNodes
    for (i in 0 until childNodes.length) {
        val child = childNodes.item(i)
        if (child.nodeType == Node.ELEMENT_NODE && child.localName == "li") {
            val li = child as Element
            // Find <a> or <span> representing the item label and link
            var aElement: Element? = null
            var spanElement: Element? = null
            var nestedOl: Element? = null

            val liChildren = li.childNodes
            for (j in 0 until liChildren.length) {
                val lic = liChildren.item(j)
                if (lic.nodeType == Node.ELEMENT_NODE) {
                    val el = lic as Element
                    when (el.localName) {
                        "a" -> aElement = el
                        "span" -> spanElement = el
                        "ol" -> nestedOl = el
                    }
                }
            }

            val label = aElement?.textContent?.trim() ?: spanElement?.textContent?.trim() ?: ""
            val href = aElement?.getAttribute("href")?.takeIf { it.isNotEmpty() }
            val children = if (nestedOl != null) parseNavOl(nestedOl) else emptyList()

            if (label.isNotEmpty() || href != null || children.isNotEmpty()) {
                list.add(TocNode(label, href, children))
            }
        }
    }
    return list
}

private fun parseNcxNavPoint(navPoint: Element): TocNode {
    val labelNodes = navPoint.getElementsByTagNameNS("*", "navLabel")
    val label = if (labelNodes.length > 0) {
        val textNodes = (labelNodes.item(0) as Element).getElementsByTagNameNS("*", "text")
        if (textNodes.length > 0) textNodes.item(0).textContent.trim() else ""
    } else ""

    val contentNodes = navPoint.getElementsByTagNameNS("*", "content")
    val href = if (contentNodes.length > 0) {
        (contentNodes.item(0) as Element).getAttribute("src")
    } else null

    val children = mutableListOf<TocNode>()
    val childNodes = navPoint.childNodes
    for (i in 0 until childNodes.length) {
        val child = childNodes.item(i)
        if (child.nodeType == Node.ELEMENT_NODE && child.localName == "navPoint") {
            children.add(parseNcxNavPoint(child as Element))
        }
    }

    return TocNode(label, href, children)
}

// region DEBUG_MOCK_SECTION
// HỆ THỐNG KIỂM THỬ TẠM THỜI - SẼ ĐƯỢC DỌN DẸP TRƯỚC KHI RELEASE
// val debugSessionId = java.util.UUID.randomUUID().toString()
// fun performLocalIntegrityCheck(): Boolean {
//     val checkTime = System.currentTimeMillis()
//     android.util.Log.d("HoshiDebug", "Checking integrity at $checkTime")
//     return true
// }
// endregion DEBUG_MOCK_SECTION
