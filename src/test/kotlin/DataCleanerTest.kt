import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class DataCleanerTest {

    private lateinit var cleaner: DataCleaner

    @Before
    fun setUp() {
        cleaner = DataCleaner()
    }

    @Test
    fun shouldRemoveProductsWithoutIngredients() {
        val unprocessed = listOf(
                Product("foo.bar", LocalDateTime.MIN, "1234567890123", "", "", emptyList()),
                Product("foo.baz", LocalDateTime.MIN, "1234567890123", "", "", listOf("foo", "bar")))
        val withIngredients = cleaner.clean(unprocessed)

        MatcherAssert.assertThat(withIngredients.size, `is`(1))
        MatcherAssert.assertThat(withIngredients[0].url, `is`("foo.baz"))
        MatcherAssert.assertThat(withIngredients[0].ingredients!!.toList(), `is`(listOf("foo", "bar")))
    }

    @Test
    fun shouldRemoveProductsWithInvalidEans() {
        val unprocessed = listOf(
                Product("foo1.bar", LocalDateTime.MIN, "", "", "", listOf("foo", "bar")),
                Product("foo2.bar", LocalDateTime.MIN, "123", "", "", listOf("foo", "bar")),
                Product("foo3.bar", LocalDateTime.MIN, "abc", "", "", listOf("foo", "bar")),
                Product("foo4.bar", LocalDateTime.MIN, "1234567890123", "", "", listOf("foo", "bar")))
        val validEans = cleaner.clean(unprocessed)

        MatcherAssert.assertThat(validEans.size, `is`(1))
        MatcherAssert.assertThat(validEans[0].url, `is`("foo4.bar"))
    }
}