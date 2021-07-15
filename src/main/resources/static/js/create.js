function create() {
    let form = $('#createForm')[0];
    let data = new FormData(form);

    $.ajax({
        type: "POST",
        url: "/core/v1/creative/",
        enctype: 'multipart/form-data',
        data: data,
        contentType: false,
        processData: false,
        cache: false
    }).done(function() {
        alert("생성 성공!");
        location.href = "/";
    }).fail(function(err) {
        let respErr = err.responseJSON;
        console.log(respErr);
        if(respErr.status === 400){
            if(respErr.errors.length === 0) {
                alert("종료일시가 시작일시보다 과거의 시간이면 안됩니다");
            } else {
                let errors = respErr.errors;
                let str = "입력값 오류 목록\n";
                for(let i = 0; i < errors.length; i++) {
                    str += "- " + errors[i].reason + "\n";
                }
                alert(str);
            }
        } else {
            alert(err.responseJSON.message);
        }
    })
}