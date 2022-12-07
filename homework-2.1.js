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

-- insert 3

db.clientes.insertMany([
 {
  "email": "ronaldo@dbccompany.com.br",
  "senha": "123",
  "primeiroNome": "Ronaldo",
  "ultimoNome": "Fenomeno",
  "cpf": "11165463344",
  "dataNascimento": new Date (1970-04-20),
  saldo: 2000.00
},
{
  "email": "julio@dbccompany.com.br",
  "senha": "123",
  "primeiroNome": "Julio",
  "ultimoNome": "Moreira Rocha",
  "cpf": "11198748744",
  "dataNascimento": new Date (1950-04-20),
   saldo: 1500.00
}
])

db.createCollection("cinemas")


db.cinemas.insertMany([
 {
  "email": "noahgmail@gmail.com.br",
  "senha": "123",
  "nome": "CINEMARK Pier-21",
  "estado": "Maranhão",
  "cidade": "Ceilândia"
},
{
  "email": "noahhotmailo@hotmail.com.br",
  "senha": "143",
  "nome": "CINEMARK Pier-23",
  "estado": "Rio Grande do Sul",
  "cidade": "Porto Alegre"
}
])

-- find 1 - clientes
db.clientes.find(
  {
    "primeiroNome":"Moises Noah"
  }
  )

-- find 2 - clientes
db.clientes.find(
  {
    "primeiroNome":"Julio Gabriel"
  }
  )

-- find expressão cliente

db.clientes.find({
    "primeiroNome": /^Moi/ 
})

-- find 1 - cinema
db.clientes.find(
  {
    "nome":"CINEMARK Pier-21"
  }
  )

-- find expressão cinema

db.clientes.find({
  "cidade": /^Po/ 
})

-- update clientes

db.clientes.updateOne(
   { primeiroNome: "Moises Noah" },
   {
     $set: { "primeiroNome": "Moises"}           
   }
)

db.clientes.updateOne(
   { primeiroNome: "Julio Rocha" },
   {
     $set: { "primeiroNome": "Julio"}           
   }
)

-- update cinema
db.cinemas.updateOne(
   { nome: "CINEMARK Pier-21" },
   {
     $set: { "cidade": "Porto Alegre"}           
   }
)

db.cinemas.updateOne(
   { nome: "CINEMARK Pier-21" },
   {
     $set: { "estado": "Rio Grande do Sul"}           
   }
)

-- project clientes

db.clientes.find({},
  {
    _id: 0,
    dataNascimento: {$dateToString: { date: "$dataNascimento", format: "%d/%m/%Y"}},
    primeiroNome: 1
  })

db.clientes.find({},
  {
    _id: 1,
    nome: {$dateToString: { date: "$dataNascimento", format: "%d/%m/%Y"}},
    primeiroNome: 1
  })


-- project cinemas

db.cinemas.find({},
  {
    _id: 0,
    nome: 1,
    cidade: 1
  })

db.cinemas.find({},
  {
    _id: 1,
    nome: 1,
    cidade: 1
  })

-- agregate clientes

db.clientes.aggregate( [
   { $group: { _id: null, saldo: {$sum: "$saldo" }} }
] )

db.clientes.aggregate( [
   {
     $group: {
        _id: null,
        count: { $sum: 1 }
     }
   }
] )

-- agregate cinemas

db.cinemas.aggregate( [
   {
     $group: {
        _id: null,
        count: { $sum: 1 }
     }
   }
] )

db.cinemas.aggregate( [
   {
     $group: {
        _id: "Rio Grande do Sul" ,
        count: { $sum: 1 }
     }
   }
] )

-- deletar clientes

db.clientes.deleteOne(
{
  primeiroNome: "Moises"
}
)

db.cinemas.deleteOne(
{
  primeiroNome: "Julio"
}
)

-- deletar cinema

db.cinemas.deleteOne(
{
  nome: "CINEMARK Pier-21"
}
)


db.cinemas.deleteOne(
{
  nome: "CINEMARK Pier-23"
}
)