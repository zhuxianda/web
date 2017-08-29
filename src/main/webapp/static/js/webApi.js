const URL = {
    saveToList : 'http://127.0.0.1:8089/saveToList.do',
}




 const webApi = {

    sendToService(state) {
        return fetch(URL.saveToList + '?queryBody=' + JSON.stringify(state))
            .then(response => response.json())


            // .then(json => {
            //     console.log(json);
            // });
    }

}

/*const webApi = {
    sendToService: function(state) {

    }
}*/


//webApi.sendToService();