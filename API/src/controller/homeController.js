const { name } = require("ejs");
const connection = require("../config/db_quanao.js");
const upload = require("../middlewares/upload.js");

// LẤY DATA TỪ KH
const getApi = async (req, res) => {
  try {
    const [rows] = await connection.query("SELECT * FROM khachhang");
    res.json({ products: rows });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};

// API ĐĂNG NHẬP
const loginApi = async (req, res) => {
  try {
    const { email, password } = req.body;

    console.log("Nhận được:", { email, password });

    if (!email || !password) {
      return res
        .status(400)
        .json({ success: false, message: "Thiếu email hoặc mật khẩu" });
    }

    const [rows] = await connection.query(
      "SELECT * FROM khachhang WHERE email = ? AND password = ? LIMIT 1",
      [email, password]
    );

    console.log("Kết quả truy vấn:", rows);

    if (rows.length > 0) {
      return res.json({
        success: true,
        message: "Đăng nhập thành công",
        user: rows[0],
      });
    } else {
      return res
        .status(401)
        .json({ success: false, message: "Email hoặc mật khẩu không đúng" });
    }
  } catch (err) {
    console.error("Lỗi trong loginApi:", err.message, err.stack);
    return res.status(500).json({ success: false, message: "Lỗi server" });
  }
};

// API ĐĂNG KÝ
const registerApi = async (req, res) => {
  try {
    const { hoten, diaChi, SDT, passWord, email, sex } = req.body;

    if (!hoten || !diaChi || !SDT || !passWord || !email || !sex) {
      return res
        .status(400)
        .json({ success: false, message: "Thiếu thông tin" });
    }
    const [rows] = await connection.query(
      `INSERT INTO khachhang (hoTen, diaChi, SDT, passWord, email, sex)
        VALUES (?, ?, ?, ?, ?, ?);`,
      [hoten, diaChi, SDT, passWord, email, sex]
    );
    if (rows.affectedRows > 0) {
      return res.json({
        success: true,
        message: "Đăng ký thành công",
        user: { email },
      });
    } else {
      return res
        .status(400)
        .json({ success: false, message: "Đăng ký thất bại" });
    }
  } catch (err) {
    console.error("Lỗi trong registerApi:", err.message, err.stack);
    return res.status(500).json({ success: false, message: "Lỗi server" });
  }
};
// API SẢN PHẨM
const productUploadApi = async (req, res) => {
  try {
    // multer đã xử lý file, req.file và req.body
    const { tenHang, DonGia } = req.body;
    const file = req.file;

    if (!file) {
      return res
        .status(400)
        .json({ success: false, message: "Chưa upload ảnh" });
    }
    if (!tenHang || !DonGia) {
      return res
        .status(400)
        .json({ success: false, message: "Thiếu tên hoặc giá sản phẩm" });
    }

    const imageUrl = `http://localhost:3000/uploads/${file.filename}`;

    const [result] = await connection.query(
      "INSERT INTO hanghoa (tenHang, gia, hinhAnh) VALUES (?, ?, ?)",
      [tenHang, DonGia, imageUrl]
    );

    // Trả về dữ liệu sản phẩm vừa tạo (có thể thêm id insertId)
    const newProduct = {
      id: result.insertId,
      tenHang,
      DonGia: Number(DonGia),
      hinhAnh: imageUrl,
    };
    return res.json({
      success: true,
      message: "Upload thành công",
      product: newProduct,
    });
  } catch (err) {
    console.error("Lỗi trong productUploadApi:", err.message);
    return res.status(500).json({ success: false, message: "Lỗi server" });
  }
};

const getProductApi = async (req, res) => {
  try {
    const [rows] = await connection.query("SELECT * FROM hanghoa");
    res.json(rows); // trả về mảng JSON
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};
// API LẤY THỂ LOẠI
const getTheLoaiApi = async (req, res) => {
  try {
    const [rows] = await connection.query("SELECT tenTheLoai FROM theloai"); // Lấy trường tenTheLoai
    res.json(rows); // trả về mảng JSON
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};
// APi Search
const Search = async (req, res) => {
  try {
    const keyword = req.query.keyword || "";
    const searchKeyword = `%${keyword}%`;
    const [rows] = await connection.query(
      `Select * from hanghoa where tenHang like ?`,
      [searchKeyword]
    );
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};
// API profile
const profileApi = async (req, res) => {
  try {
    const id = req.params.id;
    const [rows] = await connection.query(
      "SELECT * FROM khachhang WHERE maKH = ?",
      [id]
    );
    res.json(rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};
// API CHANGE PASSWORD
const changePasswordApi = async (req, res) => {
  try {
    const id = req.params.id;
    const { curPass, newPass, RenewPass } = req.body;
    const [rows] = await connection.query(
      `SELECT passWord FROM khachhang WHERE passWord = ? AND maKH = ?`,
      [curPass, id]
    );
    if (rows.length === 0) {
      return res.json({ error: "Mật khẩu cũ không đúng!" });
    }
    if (newPass !== RenewPass) {
      return res.json({ error: "Mật khẩu mới không khớp!" });
    }
    const [rows1] = await connection.query(
      `UPDATE khachhang SET passWord = ? WHERE maKH = ?`,
      [RenewPass, id]
    );
    res.json({ message: "Đổi mật khẩu thành công!", result: rows1 });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
};
// API thêm vào giỏ hàng
const InsertProCartApi = async (req, res) => {
  try {
    const id = req.params.id; // ID của khách hàng
    const [rows] = await connection.query(
      `select maKH from khachhang where maKH = ?`,
      [id]
    );
    if (rows.length > 0) {
      const { tenHang, donGia, hinhAnh, soLuong } = req.body;
      const [rows1] = await connection.query(
        `insert into giohang (tenHang, donGia ,hinhAnh,soLuong , maKH)
         values (?, ?,?, ?, ?)`,
        [tenHang, donGia, hinhAnh, soLuong, id]
      );
      if (rows1.affectedRows > 0) {
        res.json({ message: "Thêm vào giỏ hàng thành công!", result: rows1 });
      } else {
        res.json({ error: "Thêm vào giỏ hàng thất bại!" });
      }
    }
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
//1. API: GET /api/giohang/:id
const GetGioHangApi = async (req, res) => {
  try {
    const id = req.params.id;

    const [rows] = await connection.query(
      "SELECT * FROM giohang WHERE maKH = ?",
      [id]
    );

    res.json(rows);
  } catch (error) {
    console.error("Lỗi lấy giỏ hàng:", error);
    res.status(500).json({ error: error.message });
  }
};
//update so lương gio hang
const updateSoLuongGioHang = async (req, res) => {
  try {
    const idCart = req.params.id;
    const { soLuong } = req.body;

    const [result] = await connection.query(
      "UPDATE giohang SET soLuong = ? WHERE idCart = ?",
      [soLuong, idCart]
    );

    if (result.affectedRows > 0) {
      res.json({ message: "Cập nhật số lượng thành công" });
    } else {
      res.status(404).json({ error: "Không tìm thấy sản phẩm trong giỏ hàng" });
    }
  } catch (error) {
    console.error("Lỗi update:", error);
    res.status(500).json({ error: error.message });
  }
};
//xóa giỏ hàng
const deleteItemGioHang = async (req, res) => {
  try {
    const idCart = req.params.id;
    console.log("Đã nhận DELETE giỏ hàng idCart =", idCart);
    const [result] = await connection.query(
      "DELETE FROM giohang WHERE idCart = ?",
      [idCart]
    );

    if (result.affectedRows > 0) {
      res.json({ message: "Xoá thành công!" });
    } else {
      res.status(404).json({ error: "Không tìm thấy sản phẩm!" });
    }
  } catch (error) {
    console.error("Lỗi xoá giỏ hàng:", error);
    res.status(500).json({ error: error.message });
  }
};

module.exports = {
  getApi,
  loginApi,
  registerApi,
  productUploadApi,
  getProductApi,
  getTheLoaiApi,
  Search,
  profileApi,
  changePasswordApi,
  InsertProCartApi,
  GetGioHangApi,
  updateSoLuongGioHang,
  deleteItemGioHang,
};
