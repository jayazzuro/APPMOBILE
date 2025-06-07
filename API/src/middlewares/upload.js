const multer = require("multer");

const storage = multer.diskStorage({
  destination: (req, file, res) => {
    res(null, "./src/public/img");
  },
  filename: (req, file, res) => {
    cb(null, Date.now() + path.extname(file.originalname));
  },
});
const upload = multer({ storage: storage });
module.exports = upload;
