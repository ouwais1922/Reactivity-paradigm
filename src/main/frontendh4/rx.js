const RxHR = require('@akanass/rx-http-request').RxHR;
const Rx = require('rxjs');
const operators = require('rxjs/operators');
var observable$ = null;
var subscription = null;

rxFunction = function () {
    try {
        if (document.getElementById("button").value == "Start") {
            document.getElementById("button").value = "Stop";
            const number = document.getElementById("number").value * 1000;
            observable$ = RxHR.get('http://localhost:8080/tools/screenshot');
            subscription = Rx.interval(number).subscribe(
                () => {
                    observable$.pipe(
                        operators.filter(
                            (data) => data.response.statusCode === 200
                        ),
                        operators.catchError((err,caught) => {
                            alert(err);
                            return Rx.empty();
                        })
                        ).subscribe(
                            (data) => {
                                document.getElementById("ItemPreview").src = "data:image/png;base64," + data.body;
                            },
                            (err) => {
                                alert(err);
                                console.error(err);
                            }
                        )
                },
                (err) =>{
                    console.log(err);
                }
            );
        }
        else {
            document.getElementById("button").value = "Start";
            subscription.unsubscribe();
        }
    } catch (error) {
        console.log(error)
    }
};

