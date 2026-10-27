package no.nav.tsm

import org.slf4j.LoggerFactory

object OpenCV {

    private val logger = LoggerFactory.getLogger(OpenCV::class.java)
    private var isLoaded = false

    private val supportedArchTypes = listOf("aarch64", "amd64", "x86_64")
    private val supportedPlatforms = listOf("Linux", "Mac OS X")
    fun load(): Boolean {
        if(isLoaded) return true

        val os = System.getProperty("os.name")
        val arch = System.getProperty("os.arch")

        logger.info("OS: $os, arch: $arch")

        if(supportedPlatforms.contains(os)) {
            isLoaded = true
            return true
        } else {
            logger.warn("Unable to load OpenCV, $os and $arch not supported")
            return false
        }
    }
}
