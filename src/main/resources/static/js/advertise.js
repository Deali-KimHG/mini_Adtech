window.onload = function () {
    $.ajax({
        type: "GET",
        url: "/dsp/v1/advertisement/",
        contentType: "application/json"
    }).done(function(data) {
        let str = '';
        let idx = 1;
        $.each(data, function(i) {
            str += '<tr>';
            str += '<td>' + idx + '</td>';
            str += '<td>' + data[i].title + '</td>';
            str += '<td><img src="http://localhost:8081/images/' + data[i].image + '" style="width: 200px; height: 200px;"></td>';
            str += '<td>' + data[i].price + '</td>';
            str += '<td>' + data[i].advertiseEndDate + '</td>';
            str += '<td>' + data[i].updatedDate + '</td>';
            str += '<td>' + data[i].score + '</td>';
            str += '</tr>';
            idx++;
        });
        $('#creativeList').append(str);
    }).fail(function(err) {
        alert(err.responseJSON.message);
    })
}