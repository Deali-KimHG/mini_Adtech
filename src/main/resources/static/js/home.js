window.onload = function () {
    $.ajax({
        type: "GET",
        url: "/core/v1/creative/",
        contentType: "application/json"
    }).done(function(data) {
        let str = '';
        $.each(data, function(i) {
            str += '<tr>';
            str += '<td><a href="/detail/' + data[i].id + '">' + data[i].id + '</a></td>';
            str += '<td>' + data[i].title + '</td>';
            str += '<td>' + data[i].status + '</td>';
            str += '<td>' + data[i].price + '</td>';
            str += '<td>' + data[i].advertiseStartDate + '</td>';
            str += '<td>' + data[i].advertiseEndDate + '</td>';
            str += '<td><img src="http://localhost:8081/images/' + data[i].creativeImages[0].id + '.' + data[i].creativeImages[0].extension + '" style="width: 300px; height: 100px;"></td>';
            str += '<td>' + data[i].creativeCount.count + '</td>';
            str += '</tr>';
        });
        $('#creativeList').append(str);
    }).fail(function(err) {
        alert(err.responseJSON.message);
    })
}