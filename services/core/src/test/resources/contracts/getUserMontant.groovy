package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return user montant"
    request {
        method GET()
        urlPath("/user/montant/1") {
            headers {
                header('Authorization', $(regex('Bearer [a-zA-Z0-9-.]+')))
            }
        }
    }
    response {
        status OK()
        body(100.00)
        headers {
            contentType(applicationJson())
        }
    }
}
