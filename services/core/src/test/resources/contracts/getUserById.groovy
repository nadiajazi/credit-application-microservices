package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return user data by id"
    request {
        method GET()
        urlPath('/user/1')
        headers {
            header 'Authorization': $(consumer(regex('Bearer [a-zA-Z0-9-._~+/]+=*')))
        }
    }
    response {
        status 200
        body([
                id: 1,
                firstName: 'John',
                lastName: 'Doe',
                email: 'john.doe@example.com',
                montant: 100.00
        ])
        headers {
            contentType(applicationJson())
        }
    }
}
