package `in`.curioustools.menu_maker

import `in`.curioustools.menu_maker.modal.MenuEntry
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

        val it = listOf<MenuEntry>(
            MenuEntry("a"),
            MenuEntry("b"),
            MenuEntry("v"),
            MenuEntry("d"),
            MenuEntry("b"),
            MenuEntry("a"),
            MenuEntry("c"),
            MenuEntry("a")
            )
        println(it)

        val filteredStrings = it.map { it.categoryName };
        println(filteredStrings)
        val filteredList = filteredStrings.distinct() //for reducing to only original strings
        println(filteredList)

        assertEquals(4, 2 + 2)
    }

    @Test
    fun testHtml(){
        var htmlString = "<html><head>";
        htmlString +=
            "<style type=\"text/css\" media=\"print\">\n" +
                    "@page{size:  auto; margin: 0mm;}\n" +
                    "html{background-color: #FFFFFF;margin: 0px;}\n" + "body{padding: 1%;}\n" +
                    "</style>\n";
        htmlString +=
            "<style>\n" +
                    "body {background-color: #efffea; }\n" +
                    "table {margin: auto;width: calc(100% - 40px);}\n" +
                    "td {text-align: center;vertical-align: middle;}\n" +
                    "th {background-color: #8cba51;color: white;}\n" +
                    "tr{background-color: white;}\n" +
                    ".x{background-color:#efffea;}\n" +
                    ".c{color:black;}\n" +
                    "</style></head>\n";

        htmlString += "<body>\n" + "<H1> Menu </H1>" + "<table cellspacing=\"0\">\n";

        htmlString+=

            "<tr><th colspan=3 class=\"x\"><br></th></tr>\n" +
                    "<tr><th colspan=3 class=\"c\">Users</th></tr>\n" +
                    "<tr><th width=40%>ItemName</th><th>Rate(Half)</th><th>Rate(Full)</th></tr>\n" +
                    "<tr><td>P1</td><td>N1</td><td>E1</td></tr>\n" +
                    "<tr><td>P2</td><td>N2</td><td>E2</td></tr>\n" +
                    "<tr><td>P3</td><td>N2</td><td>E3</td></tr>\n" +
                    "\n" +
                    "<tr><th colspan=3 class=\"x\"><br></th></tr>\n" +
                    "<tr><th colspan=3 class=\"c\">Users</th></tr>\n" +
                    "<tr><th width=40%>ItemName</th><th>Rate(Half)</th><th>Rate(Full)</th></tr>\n" +
                    "<tr><td>P1</td><td>N1</td><td>E1</td></tr>\n" +
                    "<tr><td>P2</td><td>N2</td><td>E2</td></tr>\n" +
                    "<tr><td>P3</td><td>N2</td><td>E3</td></tr>\n" +
                    "<tr><td>P1</td><td>N1</td><td>E1</td></tr>\n" +
                    "<tr><td>P2</td><td>N2</td><td>E2</td></tr>\n" +
                    "<tr><td>P3</td><td>N2</td><td>E3</td></tr>\n" +
                    "\n" +
                    "</table>\n" +
                    "</body>\n" +
                    "</html>"
        println(htmlString)
        assertEquals(4,2+2)
    }
}