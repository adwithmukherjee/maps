<#assign content>

    <div class="body">

        ${stars}
        <div class="content">
            <div class="left">
                <h1>Star Search! </h1>

                <div class="dropdown">
                    <button class="dropbtn">Select Star Data</button>

                    <form class="dropdown-content" method="POST" action="/select-data">
                        <button name="text" type="submit" value="stardata.csv">stardata.csv</button>
                        <button name="text" type="submit" value="ten-star.csv">ten-star.csv</button>
                        <button name="text" type="submit" value="one-star.csv">one-star.csv</button>
                        <button name="text" type="submit" value="three-star.csv">three-star.csv</button>
                        <button name="text" type="submit" value="tie-star.csv">tie-star.csv</button>
                    </form>
                </div>

                <p> for neighbors
                <form method="POST" action="/neighbors">
                    <input type="checkbox" id="naive-checkbox" name="naive" value="isNaive">
                    <label for="naive-checkbox">naive</label><br>
                    <textarea autofocus name="text" id="text"></textarea></br>
                    <input type="submit" class="submit-input">
                </form>
                </p>

                <p> by radius
                <form method="POST" action="/radius">
                    <input type="checkbox" id="naive-checkbox" name="naive" value="isNaive">
                    <label for="naive-checkbox">naive</label><br>
                    <textarea name="text" id="text"></textarea></br>
                    <input type="submit" class="submit-input">
                </form>
                </p>

                ${suggestions}


            </div>

            <div class="right">
                <ul role="listbox">
                    ${cells}
                </ul>
            </div>

        </div>


    </div>



</#assign>
<#include "main.ftl">