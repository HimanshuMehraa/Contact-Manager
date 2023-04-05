//FIRST REQUEST TO SERVER TO ORDER

const paymentStart = () => {
    console.log("Payment started");
    let amount = $("#payment_field").val();
    console.log(amount);

    if (amount == "" || amount == null) {
        swal("Amount Required", "?", "warning");
        return;
    }
    //We USE AJAX TO SEND REQUESTS
    $.ajax({
        url: '/user/create-order',
        data: JSON.stringify({ amount: amount, info: 'order_request' }),
        contentType: 'application/json',
        type: 'POST',
        datatype: 'json',

        success: function(ans) {
            var response = JSON.parse(ans);
            console.log("L1");
            console.log(response);
            console.log(response.status);
            if (response.status == "created") {
                console.log("L2");
                //open payment form
                var options = {
                    key: 'rzp_test_uS8ICCHpVMs0ZG',
                    amount: response.amount,
                    currency: 'INR',
                    name: 'Smart Contact Manager',
                    description: 'Donation',
                    image: 'https://st2.depositphotos.com/3559981/6255/i/600/depositphotos_62552689-stock-photo-photo-of-man-wearing-vendetta.jpg',
                    order_id: response.id,

                    handler: function(response) {
                        console.log(response.razorpay_payment_id);
                        console.log(response.razorpay_order_id);
                        console.log(response.razorpay_signature);
                        console.log("Payment successful");
                        swal("Congrats!", "Payment Successful", "success");

                    },
                    prefill: {
                        name: "", //your customer's name
                        email: "",
                        contact: ""
                    },
                    notes: {
                        address: "FT"
                    },
                    theme: {
                        color: "#3399cc"
                    }
                };

                let rzp = new Razorpay(options);
                rzp.on('payment.failed', function(response) {
                    console.log(response.error.code);
                    console.log(response.error.description);
                    console.log(response.error.source);
                    console.log(response.error.step);
                    console.log(response.error.reason);
                    console.log(response.error.metadata.order_id);
                    console.log(response.error.metadata.payment_id);
                    swal("Oops", "Payment Failed", "error");
                });
                rzp.open();
            } else {
                console.log("L3");
            }
        },

        error: function(error) {
            alert("Error: something wrong");
        }


    })
};