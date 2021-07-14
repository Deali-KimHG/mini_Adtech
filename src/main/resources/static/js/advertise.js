window.onload = function () {
    $.ajax({
        type: "GET",
        url: "/dsp/v1/advertisement/",
        contentType: "application/json"
    }).done(function(data) {
        let str = '';
        $.each(data, function(i) {
            str += '<tr>';
            str += '<td>' + data[i].title + '</td>';
            str += '<td><img src="http://localhost:8081/images/' + data[i].image + '" style="width: 300px; height: 100px;"></td>';
            str += '<td>' + data[i].price + '</td>';
            str += '<td>' + data[i].createdDate + '</td>';
            str += '<td>' + data[i].updatedDate + '</td>';
            str += '<td>' + data[i].score + '</td>';
            str += '</tr>';
        });
        $('#creativeList').append(str);
    }).fail(function(err) {
        alert(err.responseJSON.message);
    })
}