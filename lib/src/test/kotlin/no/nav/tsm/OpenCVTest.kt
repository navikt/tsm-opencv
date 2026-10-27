package no.nav.tsm

import kotlin.test.Test
import kotlin.test.assertTrue

class OpenCVTest {
    @Test fun openCVLoadTest() {
        val loaded = OpenCV.load()
        assertTrue(loaded, "load should return 'true'")
    }
}
