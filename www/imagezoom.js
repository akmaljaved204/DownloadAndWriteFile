var imagezoom = {
    createEvent: function(fileUrl,fileName,dataObject,successCallback, errorCallback) {
        cordova.exec(
            successCallback, // success callback function
            errorCallback, // error callback function
            'Imagezoom', // mapped to our native Java class called "CalendarPlugin"
            'writeFile', // with this action name
            [{                  // and this array of custom arguments to create our entry
                "fileUrl":fileUrl,
                "fileName":fileName,
                "dataObject":dataObject
                
            }]
        ); 
    }
};
module.exports = imagezoom;