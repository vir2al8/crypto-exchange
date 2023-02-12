# API

## Функции (endpoints)

1. create, read, update, delete для криптовалют
2. read, update для кошелька
3. create, read, delete(?) для транзакций
4. create, read, update, delete для ордеров
5. create, update, delete для пользователей

## Описание сущности crypto

1. id
2. fullName
3. name
4. description
5. capitalization
6. numberOfCoins

## Описание сущности transaction

1. id
2. orderId
3. createdAt
4. updatedAt
5. capitalization

## Описание сущности fiatCurrency

1. id
2. fullName
3. name
4. description
5. rates

## Описание сущности order

1. id
2. purchaseCurrency (BTC, USDT)
3. userId (buyer)
4. userId (salesman)
5. OrderType: market/limit
6. OperationType: purchase/sale
7. count
8. price (limit or current currency)
9. status: open/closed
10. createdAt
11. updatedAt

## Описание сущности user

1. id
2. firstName
3. secondName
4. middleName
5. walletId

## Описание сущности wallet

1. id
2. currency (maybe only one currency)
3. blocked (maybe delete it)
4. amount
