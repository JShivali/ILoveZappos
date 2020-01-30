# ILoveZappos
Coding Challenge for Zappos intern position

## App overview:  
The CryptoExchange app has 3 tabs each showing the transaction history, order book and price alert. The transaction history tab shows the graph of price versus time. The current bitcoin price is shown above the graph. The order book shows the tables for asks and bids and relevant information. 
The price alert tab lets the user set a limit and enable notification if the price drops below the set threshold. 


## Design decisions: 

1. The graph is plotted for all of the data that is received from the API. There is no segregation of day wise, month wise and yearly transaction history. 

2. The value of threshold is stored in Realtime database so that when the user installs application in another phone, he can still receive notification. Note: here the login/ registration has been not been implemented as this is a prototype app. This app just updates a single value in firebase database for all users (for prototype purpose). For the actual idea to work, the user needs to login into the app and the app has to update threshold only for specific user to receive notifications. 

3. For the price alert notification, I have used the WorkManager instead of the FirebaseJobDispatcher as it is deprecated and requires google play services to be installed as a prerequisite. 


## Implementation details: 

The transaction tab shows the transaction history data in the form of a line graph. The line graph shows fixed number of values at once. To view more data the line graph can be slide horizontally. I have used MPAndroidChart library for showing this data. Above the graph, the user can see the current bitcoin price. 

The data for the order book screen rows is shown as per the reference given (https://www.bitstamp.net/market/tradeview/). The rows are kept simple as per the given reference. The data in the recycler views will be refreshed when the fragment is visible to the user. 

The price alert fragment lets the user set a threshold value for bit coin price. Every hour the given API is hit and the stored threshold value is compared with the current price. If the current price dips below the set threshold, the user would be notified through an alert. I have used the firebase real time data base to store the threshold value. 


## Features: 

1. Animation: Graph animation while graph is loaded 

2. Retrofit to handle API requests 

3. Firebase Realtime database used to store price limit 

4. WorkManager to schedule periodic job and send notification. 

5. Material UI 

## Devices: 

Pixel 3a (Pie) 

Samsung S9+ (Pie) 

Moto G5 S+ (Oreo) 

 
