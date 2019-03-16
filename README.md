# REST API Money-Transfer

<h2>Run application</h2>
mvn package exec:exec@run-app

<h2>Run tests</h2>
1. Run application: mvn package exec:exec@run-app <br/>
2. Run tests: mvn verify <br/>

<h2>Tech stack</h2>
<ul>
	<li>Java 8</li>
	<li>Vertx</li>
	<li>H2 Database (in-memory)</li>
	<li>Maven</li>
</ul

<h2>Transfer flow</h2>
<ol>
	<li>Create transfer</li>
	<li>Reserve amount on account transfering from</li>
	<li>Deposit money on account transfering to</li>
	<li>Finish transfer</li>
</ol>

Cases: <br/>
Step 1. If accounts doesn't exist transfer will not be created. <br/>
Step 2. If there is not enough balance on account transfering from, transfer will be failed<br/>
Step 3. If deposit money on account transfering to failed, return reserved money on account transfering from, set transfer to failed <br/>


<h2>End points</h2>

=== GET/accounts - Get all accounts

=== GET/accounts/{id} - Get account with id

=== POST/accounts - Create new account<br/>
Request example: <br/>
{
	"name": "Ana",
	"balance":"500.00",
	"currency":"EUR"
} <br/>
Response example: <br/>
{
    "id": 1,
    "name": "Ana",
    "balance": 500,
    "currency": "EUR"
}

=== PUT/accounts - Update balance on the account <br/>
Request/Response example: <br/>
{
    "id": 1,
    "name": "Jelena1",
    "balance": 1000,
    "currency": "EUR"
}

=== GET/transfers - Get all transfers

=== GET/transfers/{id} - Get transfer with id

=== POST/transfers - Create new transfer<br/>
Request example:
{
	"fromId": 1,
	"toId": 2,
	"amount":50,
	"transferStatus":"CREATED"
} <br/>
Response example:
{
    "id": 1,
    "fromId": 1,
    "toId": 2,
    "amount": 50,
    "transferStatus": "CREATED"
}

PUT/transfers - Update transfer status <br/>
Request/response example:
{
    "id": 6,
    "fromId": 1,
    "toId": 2,
    "amount": 5,
    "transferStatus": "FINISHED"
}

