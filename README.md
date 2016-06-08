# BusinessLogicServices

The Business Logic Service is a RestFul Web Service. This layer is responsible for all kinds of data manipolation, decision, making, calculations, etc. It receives request from the [User Interface](https://github.com/introSDE-FinalProject-2016/Telegram-Bot) layer and the [Process Centric Services](https://github.com/introSDE-FinalProject-2016/ProcessCentricServices) module; and gets data from the [Storage Services](https://github.com/introSDE-FinalProject-2016/StorageServices) module to send results back.


[API Documentation](http://docs.businesslogicservices.apiary.io/#)  
[URL Client](https://github.com/introSDE-FinalProject-2016/Telegram-Bot)  
[URL Server (heroku)](https://fierce-sea-36005.herokuapp.com/sdelab/businessLogic-service) 


###Install
In order to execute this server locally you need the following technologies:

* Java (jdk 1.8.0)
* Ant (version 1.9.6)

Then, clone the repository. Run in your terminal:

```
$ git clone like https://github.com/introSDE-FinalProject-2016/BusinessLogicServices.git && cd BusinessLogicServices
```

and run the following command:
```
ant install
```

###Getting Started
To run the server locally then run:
```
ant start
```
