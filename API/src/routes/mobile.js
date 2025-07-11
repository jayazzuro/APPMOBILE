  const express = require("express");
  const router = express.Router();
  const homeController = require("../controller/homeController");
  const upload = require("../middlewares/upload");

  // GET
  router.get("/api", homeController.getApi);
  router.get("/api/getProductApi", homeController.getProductApi);
  router.get("/api/getTheLoaiApi", homeController.getTheLoaiApi);
  router.get("/api/Search", homeController.Search);
  router.get("/api/profileApi/:id", homeController.profileApi);
  router.get("/api/giohang/:id", homeController.GetGioHangApi);
  router.get("/api/handleVNPayReturn", homeController.handleVNPayReturn);
  router.get("/api/handleVNPayIPN", homeController.handleVNPayIPN);
  router.get("/api/handleMoMoReturn", homeController.handleMoMoReturn);
  router.get("/api/momo_return", homeController.handleMoMoReturn);


  router.get(
    "/api/productUploadApi",
    upload.single("image"),
    homeController.productUploadApi
  );

  // POST
  router.post("/api/loginApi", homeController.loginApi);
  router.post("/api/registerApi", homeController.registerApi);
  router.post("/api/changePasswordApi/:id", homeController.changePasswordApi);
  router.post("/api/InsertProCartApi/:id", homeController.InsertProCartApi);
  router.post("/api/thanhToanApi/:id", homeController.thanhToanApi);
  router.post("/api/create_payment_url/:id", homeController.createVNPayPayment);
  router.post("/api/create_momo_payment/:id", homeController.createMoMoPayment);


  //put
  router.put("/api/giohang/:id", homeController.updateSoLuongGioHang);

  //deletete giỏ hàng
  router.delete("/api/giohang/:id", homeController.deleteItemGioHang);
  router.delete("/api/giohang/clear/:id", homeController.clearCartApi);

  module.exports = router;
