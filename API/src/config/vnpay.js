const { hash, HashAlgorithm,ignoreLogger,VNPay } = require("vnpay");

module.exports = {
  vnp_TmnCode: 'LPLWFGFE',
  vnp_HashSecret: '2UHT9JX1331681F4CDHPZROIVHK4419X',
  vnp_Url: 'https://sandbox.vnpayment.vn/paymentv2/vpcpay.html', // URL của VNPAY để gửi yêu cầu thanh toán
  vnp_ReturnUrl: 'https://192.168.1.129:3000/api/vnpay_return', // URL VNPAY redirect về sau khi thanh toán
  vnp_IpnUrl: 'https://192.168.1.129:3000/api/vnpay_ipn', // URL VNPAY gửi thông báo IPN

};
