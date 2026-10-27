package no.nav.tsm

import org.slf4j.LoggerFactory
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object OpenCV {

    private val logger = LoggerFactory.getLogger(OpenCV::class.java)
    private var isLoaded = false

    @Synchronized
    fun load(): Boolean {
        if(isLoaded) return true

        val os = System.getProperty("os.name")
        val arch = System.getProperty("os.arch")

        logger.info("OS: $os, arch: $arch")

        val platform = platform(os, arch)
        if(platform == null) {
            logger.warn("Unable to load OpenCV, $os and $arch not supported")
            return false
        }
        val libName = if(platform.startsWith("macos")) "libopencv_java500.dylib" else "libopencv_java500.so"

        val resource = OpenCV::class.java.getResourceAsStream("/natives/$platform/$libName")
        if(resource == null) {
            logger.warn("Unable to load OpenCV, /natives/$platform/$libName not found on classpath")
            return false
        }

        // System.load needs a real file, so copy the library out of the jar
        val tmp = Files.createTempFile("opencv_java500", ".${libName.substringAfterLast('.')}")
        tmp.toFile().deleteOnExit()
        resource.use { Files.copy(it, tmp, StandardCopyOption.REPLACE_EXISTING) }
        System.load(tmp.toAbsolutePath().toString())

        isLoaded = true
        return true
    }

    // Must match the natives/<platform> folders in lib/build.gradle.kts
    private fun platform(os: String, arch: String): String? {
        val osId = when {
            os.startsWith("Linux") -> "linux"
            os.startsWith("Mac") -> "macos"
            else -> return null
        }
        val archId = when(arch) {
            "amd64", "x86_64" -> "x86_64"
            "aarch64", "arm64" -> "aarch64"
            else -> return null
        }
        return "$osId-$archId"
    }
}
