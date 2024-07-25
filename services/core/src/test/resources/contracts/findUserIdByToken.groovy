package contracts

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    description "should return user id by token"
    request {
        method GET()
        urlPath("/user/byToken") {
            headers {
                header('Authorization', $(regex('Bearer [a-zA-Z0-9-.]+')))
            }
        }
    }
    response {
        status OK()
        body(1)
        headers {
            contentType(applicationJson())
        }
    }
}
