# REST API Money-Transfer

<h1>Run application</h1>
mvn package exec:exec@run-app

<h1>Tech stack</h1>
Java 8
Vertx
H2 Database (in-memory)
Maven

<h1>Payment flow</h1>

<h1>End points</h1>

GET/accounts - Get all accounts

GET/accounts/{id} - Get account with id

POST/accounts - Create new account
Request example:
{
	"name": "Ana",
	"balance":"500.00",
	"currency":"EUR"
}

Response example:
{
    "id": 1,
    "name": "Ana",
    "balance": 500,
    "currency": "EUR"
}

PUT/accounts - Update balance on the account
Request example:

Response example:

GET/transfers - Get all transfers

GET/transfers/{id} - Get transfer with id

POST/transfers - Create new transfer
Request example:
{
	"fromId": 1,
	"toId": 2,
	"amount":50,
	"transferStatus":"CREATED"
}

Response example:
{
    "id": 1,
    "fromId": 1,
    "toId": 2,
    "amount": 50,
    "transferStatus": "CREATED"
}

PUT/transfers - Update transfer status

