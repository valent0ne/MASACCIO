var REST_API = "http://localhost:8080/masaccio/api";
var WS = "http://localhost:8081/masaccio/ws/messages";
var stompClient = null;
var index = 0;

var app = new Vue({
        el: '#page-wrapper',
        data: {
            areas: null,
            area: null,
            message: null,
            display_info: true,
            display_error: false,
            display_gallery: true,
            display_area_container: false,
            display_area_info: false,
            display_sensor_readings: false,
            display_sensor_readings_graph: false
        },

        methods: {
            getAreas: function () {
                var that = this;
                console.log("calling: " + REST_API + '/areas/');
                axios.get(REST_API + '/areas/')
                    .then(function (response) {
                        console.log(response);
                        that.message = response.message;
                        if (that.message === "KO") {
                            that.display_error = true;
                            that.display_gallery = false;
                            return
                        }
                        that.areas = response.data.data
                    })
                    .catch(function (reason) {
                        that.display_error = true;
                        that.display_gallery = false;
                    })
            },
            goToArea: function (event) {
                var that = this;
                var target = event.currentTarget.id;
                console.log("calling: " + REST_API + '/areas/' + target);
                axios.get(REST_API + '/areas/' + target)
                    .then(function (response) {
                        console.log(response);
                        that.message = response.message;
                        if (that.message === "KO") {
                            that.display_error = true;
                            return
                        }
                        that.area = response.data.data
                    })
                    .catch(function (reason) {
                        that.display_error = true;
                    });
                that.display_gallery = false;
                that.display_info = false;
                that.display_area_container = true;
                that.display_area_info = true;
                that.display_sensor_readings = true;
                that.display_sensor_readings_graph = true;

                //connect to websocket
                wsConnect(target);

            },

    },
    mounted: function () {this.getAreas()}
});

function wsConnect(target) {
    let socket = new SockJS(WS);
    stompClient = Stomp.over(socket);

    console.log("connecting to: " + '/topic/messsages/' + target);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        //create chart
        let chart = createChart();

        stompClient.subscribe('/topic/messages/' + target, function (message) {
            console.log("received message: " + message);
            displayMessage(JSON.parse(message.body), chart);
        });
    });
}

function deployError() {
    app.data.display_error = true;
    app.data.display_area_info = false;
    app.data.display_gallery = false;
    app.data.display_info = false;
    app.data.display_area_container = false;
    app.data.display_sensor_readings = false;
    app.data.display_sensor_readings_graph = false;
}

function displayMessage(data, chart){
    let table = document.getElementById("sensors_readings_table_body");

    // create row at 1st position
    let row = table.insertRow(0);

    // Insert new cells (<td> elements) at the 1st and 2nd position of the "new" <tr> element:
    let num = row.insertCell(0);
    let sId = row.insertCell(1);
    let payload = row.insertCell(2);
    let timestamp = row.insertCell(3);

    // Add some text to the new cells:
    index += 1;
    num.innerHTML = index;
    sId.innerHTML = data.sensorId;
    payload.innerHTML = data.payload;
    timestamp.innerHTML = data.timestamp;

    addDataToChart(chart, data.timestamp, data.payload);
    if(index > 20){
        removeDataFromChart(chart);
    }

}

//TODO ADD DATASET FOR EACH SENSOR

function createChart() {
    var config = {
        type: 'line',
        data: {
            labels: [index],
            datasets: [{
                label: "sensor 1",
                data: [{
                    x: 0,
                    y: 0
                }]
            }]

        },
        options: {
            scales: {
                xAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: "timestamp"
                    }
                }],
                yAxes: [{
                    scaleLabel: {
                        display: true,
                        labelString: "value"
                    }
                }]

                }
            }
        };
    var ctx = document.getElementById("sensors_chart").getContext("2d");
    var chart = new Chart(ctx, config);
    return chart;
}

function addDataToChart(chart, label, data) {
    chart.data.labels.push(label);
    chart.data.datasets.forEach((dataset) => {
        dataset.data.push(data);
    });
    chart.update();
}

function removeDataFromChart(chart) {
    chart.data.labels.reverse().pop();
    chart.data.datasets.forEach((dataset) => {
        dataset.data.reverse().pop();
    });
    chart.update();
}


