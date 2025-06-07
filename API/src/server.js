require("dotenv").config();
const express = require("express");
const configViewEngine = require("./config/viewEngine");
const webRouter = require("./routes/mobile");
const path = require("path");

const app = express();
const port = process.env.PORT || 8888;
const hostname = process.env.HOST_NAME;

// config template engine
configViewEngine(app);

// Khai báo hình ảnh
app.use("/img", express.static(path.join(__dirname, "public/img")));

// Khai báo route
app.use("/", webRouter);

app.listen(port, hostname, () => {
  console.log(`http://localhost:${port}/api`);
});
