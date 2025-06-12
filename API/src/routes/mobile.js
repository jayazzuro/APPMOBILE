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

module.exports = router;
