var API = "http://localhost:8080/masaccio/api";

var area_loader = new Vue({
    el: '#page-wrapper',
    data: {
        areas: null,
        message: null,
        display_error: false,
        display_gallery: !this.display_error
    },

    methods: {
        getAreas: function () {
            var that = this;
            axios.get(API+'/areas/')
                .then(function (response) {
                    that.message = response.message;
                    if (that.message === "KO"){
                        that.display_error = true;
                        return
                    }
                    that.areas = response.data.data
                })
                .catch(function (reason) {
                    that.display_error = true
                })
        }
    },
    mounted:function(){
        this.getAreas()
    }
})