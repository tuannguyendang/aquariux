# Trade Service

### Description
1. Develop a crypto trading system with SpringBoot framework and in memory H2
Database.
2. Design the table structure based on the understanding of the questions.

### Functional
1. User able to buy/sell the supported crypto trading pairs
2. User able to see the list of trading transactions
3. User able to see the crypto currencies wallet balance

### Non-Functional
* Availability: no downtime
* Fault tolerance: fault tolerance and fast recovery
* Latency: round-trip should be in the millisecond
* Security: KYC (Know your client) check to verify user before new account opened, prevent DDos
* Performance: (It is enhancement feature)
  * use memory database like Redis (cluster) for handle millions of request for wallet service.
  * use Event Sourcing to build feature view transaction history in wallet  

### High Level Design
![trade_system_high_level_design.png](trade_system_high_level_design.png)
* In scope of this project we will: Design and implement trade service only. So in this service we need store user wallet information for funds check, sale/buy... 
* Other microservices will do in enhancement.

### Data Model
![trade_system_date_model.drawio.png](trade_system_date_model.drawio.png)

### API Design
1. Retrieve Latest Best Aggregated Price : 
   - GET /v1/trade/aggregation-prices/latest
2. Trade Based on Latest Best Aggregated Price
   - POST /v1/trade
3. Retrieve Wallet Balance
   - GET /v1/trade/wallet/{userId}
4. Retrieve Trade History
   - GET /v1/trade/history/{userId}

### Testing
- Run unit test
![runtest1.png](runtest1.png)

#### API Manual testing:
- by postman:\
import file [Interview.postman_collection.json](Interview.postman_collection.json) to your postman
- by CURL:
  - API Best Aggregated Prices:\
    ``` curl --location 'http://localhost:8080/v1/trade/aggregation-prices/latest' ```
  - API Trade Based on Latest Best Aggregated Price:\
    ``` curl --location 'http://localhost:8080/v1/trade/ac864ed4-bd3d-4ca0-8ba2-b49ec74465ff' \--header 'Content-Type: application/json' \--data '{"tradingPair": "ETHUSDT", "orderType": "","quantity": 11.09 }' ```
  - API Retrieve Wallet Balance:\
    ``` curl --location 'http://localhost:8080/v1/trade/wallet/ac864ed4-bd3d-4ca0-8ba2-b49ec74465ff' ```
  - API Trade history:\
    ``` curl --location 'http://localhost:8080/v1/trade/history/ac864ed4-bd3d-4ca0-8ba2-b49ec74465ff' ```