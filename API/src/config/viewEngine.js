const path = require("path");
const express = require("express");
const session = require("express-session");
const cors = require("cors");
const configViewEngine = (app) => {
  app.set("views", path.join("./src", "views"));
  app.set("view engine", "ejs");
  // config static file
  app.use(express.static(path.join("./src", "public")));
  // config res.body
  app.use(express.json()); // for json
  app.use(express.urlencoded({ extended: true })); // for form data
  // session
  app.use(
    session({
      secret: "secret",
      resave: false,
      saveUninitialized: true,
      cookie: { secure: false },
    })
  );
  // COR
  app.use(cors());
};

module.exports = configViewEngine;
