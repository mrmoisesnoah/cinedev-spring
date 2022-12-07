use cinedev

db.createCollection("clientes")

-- insert 1
db.clientes.insert(
 {
  "email": "noahbispo@yahoo.com.br",
  "senha": "123",
  "primeiroNome": "Moises Noah",
  "ultimoNome": "Silva Bispo",
  "cpf": "11122233344",
  "dataNascimento": new Date (1999-02-20)
}
  )

-- insert 2
db.clientes.insert(
  {
  "email": "julio.gabriel@dbccompany.com.br",
  "senha": "123",
  "primeiroNome": "Julio Gabriel",
  "ultimoNome": "Moreira Rocha",
  "cpf": "11198763344",
  "dataNascimento": new Date (1996-04-20)
}
  )

-- find 1
db.clientes.find(
  {
    "primeiroNome":"Moises Noah"
  }
  )

-- find 2
db.clientes.find(
  {
    "primeiroNome":"Julio Gabriel"
  }
  )
