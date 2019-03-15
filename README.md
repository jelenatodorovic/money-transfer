# REST API Money-Transfer

<h2>Run application</h2>
mvn package exec:exec@run-app

<h2>Tech stack</h2>
Java 8
Vertx
H2 Database (in-memory)
Maven

<h2>Payment flow</h2>

<h2>End points</h2>

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

