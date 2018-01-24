var REST_API = "http://localhost:8080/masaccio/api";
var WS = "http://localhost:8081/masaccio/ws/messages";
var stompClient = null;
var index = 0;
var treshold = 20;

var app = new Vue({
        el: '#page-wrapper',
        data: {
            areas: null,
            area: null,
            graphs: null,
            message: null,
            sensors: null,
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
                        that.deployError(reason);
                    })
            },
            goToArea: function (event) {
                var that = this;
                let target = event.currentTarget.id;
                console.log("calling: " + REST_API + '/areas/' + target);
                axios.get(REST_API + '/areas/' + target)
                    .then(function (response) {
                        console.log(response);
                        that.message = response.message;
                        if (that.message === "KO") {
                            that.display_error = true;
                            return
                        }
                        that.area = response.data.data;
                        console.log("retrieved data of area: "+that.area.name);
                        console.log("displaying area page");
                        that.displayAreaPage();
                        console.log("retrieving sensors");
                        that.getSensorsByArea().then(function (charts) {
                            console.log("charts from getSensorsByArea "+charts);
                            //connect to websocket
                            wsConnect(target, charts);
                        });
                    })
                    .catch(function (reason) {
                        that.deployError(reason);
                    });


            },
            getSensorsByArea: function () {
                var that = this;
                let charts = [];
                console.log("calling: " + REST_API + '/areas/' + that.area.name + '/sensors');
                return axios.get(REST_API + '/areas/' + that.area.name + '/sensors')
                    .then(function (response) {
                        console.log(response);
                        that.message = response.message;
                        if (that.message === "KO") {
                            that.display_error = true;
                            that.display_gallery = false;
                            return
                        }
                        that.sensors = response.data.data;
                        console.log("retrieved sensors: "+that.sensors);
                        that.sensors.forEach(function(entry) {
                            console.log("creating graph for sensor: "+entry.id);
                            charts[entry.id] = createChart(entry);
                            console.log("added chart "+ entry.id+": "+charts[entry.id]);
                            console.log("created chart in getSensorsByArea: "+charts);
                        });
                        return charts;
                    })
                    .catch(function (reason) {
                        that.deployError(reason);
                    });
            },
            deployError: function(reason) {
                console.log("deploying error... "+reason);
                this.display_error = true;
                this.display_area_info = false;
                this.display_gallery = false;
                this.display_info = false;
                this.display_area_container = false;
                this.display_sensor_readings = false;
                this.display_sensor_readings_graph = false;
            },

            displayAreaPage: function() {
                console.log("displaying area page...");
                this.display_error = false;
                this.display_area_info = true;
                this.display_gallery = false;
                this.display_info = false;
                this.display_area_container = true;
                this.display_sensor_readings = true;
                this.display_sensor_readings_graph = true;

            }
    },
    mounted: function () {this.getAreas()}
});

function wsConnect(target, charts) {
    let socket = new SockJS(WS);
    stompClient = Stomp.over(socket);

    console.log("charts in wsConnect: "+charts);

    console.log("connecting to: " + '/topic/messsages/' + target);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/topic/messages/' + target, function (message) {
            let msg = JSON.parse(message.body);
            console.log("received message: " + msg.sensorId);
            console.log("sensorId: "+msg.sensorId);
            console.log("chart inside subscribe: "+charts[msg.sensorId]);
            displayMessage(msg, charts[msg.sensorId]);
        });
    });
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

    console.log("adding data from sensor "+data.sensorId+" to chart "+chart);

    addDataToChart(chart, data.timestamp, data.payload);
    if(index > treshold){
        let rowCount = table.rows.length;
        table.deleteRow(rowCount -1);
    }

    if(chart.data.datasets[0].data.length > treshold){
        removeDataFromChart(chart);
    }
}


function createChart(item) {
    let rgb1 = getRandomInt(0,255);
    let rgb2 = getRandomInt(0,255);
    let rgb3 = getRandomInt(0,255);

    let config = {
        type: 'line',
        data: {
            labels: [index],
            datasets: [{
                label: "sensor "+item.id,
                data: [{
                    x: 0,
                    y: 0
                }],
                backgroundColor: 'rgba('+rgb1+', '+rgb2+', '+rgb3+', 0.2)',
                borderColor: 'rgba('+rgb1+', '+rgb2+', '+rgb3+', 0.3)',
            }],
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

    let chartsContainer = document.getElementById("display_sensor_readings_graph");
    let chartContainer = document.createElement('canvas');
    chartContainer.id = "sensors_chart_"+item.id;

    chartsContainer.appendChild(chartContainer);

    let ctx = document.getElementById("sensors_chart_"+item.id).getContext("2d");
    let chart = new Chart(ctx, config);

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

    chart.data.labels.shift(); // remove the label first

    chart.data.datasets.forEach(function(dataset) {
        dataset.data.shift();
    });

    chart.update();
}


function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}



