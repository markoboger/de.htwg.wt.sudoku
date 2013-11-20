root = exports ? this

root.fill_grid = (data)->
    for row,i in data.grid
        for cell,j in row
            fillCell j, i, cell

fillCell = (x, y, cell)->
    return if cell.cell.given # cell is prefilled by game field generator and cannot be changed
    if cell.cell.value isnt 0 # cell has been filled with the right number
        setCellValue(x, y, cell)
    else if cell.cell.value is 0 and cell.cell.showCandidates is true #cell has not been filled yet but candidates get showed
        setCandidates(x, y, cell)
    else # do nothing, (maybe later we have an option which makes the candidates dissappear without filling in a number)

setCellValue = (x, y, cell)->
    getCell(x,y).find(".cell").html("<font size=\"8\">#{cell.cell.value}</font>")

setCandidates = (x, y, cell)->
    s = "<font size=\"8\"></font><span class=\"candidate\" align=\"center\">"
    gridSize = Math.sqrt cell.candidates.length
    for num1 in [0..(gridSize-1)]
        s += "<span>"
        for num2 in [0..(gridSize-1)]
            number = ""
            if cell.candidates[num1*gridSize + num2]
                number = num1*gridSize + num2 + 1
            s += "<div class=\"candidatecell\" ondblclick=\"setValue(#{y},#{x},#{num1*gridSize + num2 + 1})\"><p class=\"candidatetext\">#{number}</p></div>"
        s += "</span><span>"
    s += "</span></span></span>"

    getCell(x,y).find(".cell").html(s)

getCell = (x, y)->
    $("div.grid").children().eq(y).children().eq(x)
