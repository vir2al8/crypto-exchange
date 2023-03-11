# API

## Функции (endpoints)

1. create, read, search, update, delete для валют
2. read, search, update для кошелька
3. create, read, update, delete для пользователей
4. read, search для транзакций
5. create, read, search, delete для ордеров

## Описание сущности currency

1. id
2. fullName
3. name
4. description
5. type
6. created_at

## Описание сущности user

1. id
2. firstName
3. secondName
4. middleName
5. created_at
6. updated_at

## Описание сущности wallet

1. id
2. currencyId
3. userId
4. amount
5. created_at
6. updated_at

## Описание сущности order

1. id
2. walletId
3. userId
4. Type: market/limit
5. Operation: purchase/sale
6. amount
7. status: open/closed
8. createdAt
9. updatedAt


## Описание сущности transaction

1. id
2. buyOrderId
3. sellOrderId
4. createdAt