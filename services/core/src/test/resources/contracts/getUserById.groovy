/*package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return user data by id"
    request {
        method GET()
        urlPath('/api/v1/user/1')
        headers {
            header 'Authorization': $(consumer(regex('Bearer [a-zA-Z0-9-._~+/]+=*')))
        }
    }
    response {
        status 200
        body([
                id: 1,
                firstname: 'Nadia',
                lastname: 'Jazi',
                email: 'jazinadia@gmail.com',
                phone: "1234567890"
        ])
        headers {
            contentType(applicationJson())
        }
    }
}*/