var request = new XMLHttpRequest();
function searchInfo(user) {
    var startDate = document.date.startDate.value;
    var endDate = document.date.endDate.value;
    var url = "schedule?start=" + startDate + "&end=" + endDate;
    if(!user) {
        var indexEmpl = document.getElementById("employee").options.selectedIndex;
        var employeeId = document.getElementById("employee").options[indexEmpl].value;
        url += "&employee=" + employeeId;
    }
    try {
        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                var val = request.responseText;
                document.getElementById("schedule").innerHTML = val;
            }
        }
        request.open("GET", url);
        request.send();
    } catch (e) {
        alert("Unable connect to server");
    }
}
function loadPage(url) {
    try {
        request.onreadystatechange = function () {
            if (request.readyState == 4) {
                var val = request.responseText;
                document.getElementById("data").innerHTML = val;
            }
        }
        request.open("GET", url);
        request.send();
    } catch (e) {
        alert("Unable connect to server");
    }
}
