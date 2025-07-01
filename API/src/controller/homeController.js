const { name } = require("ejs");
const connection = require("../config/db_quanao.js");
const upload = require("../middlewares/upload.js");
const moment = require("moment");
const crypto = require("crypto");
const qs = require("qs");
const axios = require("axios");
const vnpConfig = require("../config/vnpay.js");


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
// API THANH TOÁN
// ✅ API thanh toán COD
const thanhToanApi = async (req, res) => {
  try {
    const id = req.params.id;
    const { tongTien, phuongThuc } = req.body;

    const [gioHang] = await connection.query(
      "SELECT soLuong FROM giohang WHERE maKH = ?",
      [id]
    );
    const tongSL = gioHang.reduce((sum, item) => sum + item.soLuong, 0);

    await connection.query(
      `INSERT INTO dathang (idKH, TongSL, TongTien) VALUES (?, ?, ?)`,
      [id, tongSL, tongTien]
    );

    await connection.query(`DELETE FROM giohang WHERE maKH = ?`, [id]);

    res.json({ success: true, message: "Đặt hàng thành công!" });
  } catch (err) {
    console.error("Lỗi thanh toán:", err.message);
    res.status(500).json({ error: "Lỗi khi xử lý thanh toán" });
  }
};


// VNPAY PAYMENT

const createVNPayPayment = async (req, res) => {
  const ipAddr = '192.168.1.129';

  const { amount, orderInfo, bankCode } = req.body;
  const { id } = req.params;
  const date = new Date();
  const createDate = moment(date).format("YYYYMMDDHHmmss");
  const orderId = moment(date).format("HHmmssSSS");

  let vnp_Params = {
    vnp_Version: "2.1.0",
    vnp_Command: "pay",
    vnp_TmnCode: vnpConfig.vnp_TmnCode,
    vnp_Locale: "vn",
    vnp_CurrCode: "VND",
    vnp_TxnRef: orderId,
    vnp_OrderInfo: orderInfo || `TT-${id}`,
    vnp_OrderType: "other",
    vnp_Amount: amount * 100,
    vnp_ReturnUrl: vnpConfig.vnp_ReturnUrl,
    vnp_IpAddr: ipAddr,
    vnp_CreateDate: createDate,
    vnp_IpnUrl: vnpConfig.vnp_IpnUrl,
  };

  if (bankCode) {
    vnp_Params["vnp_BankCode"] = bankCode;
  }

  vnp_Params = sortObject(vnp_Params);

  const signData = qs.stringify(vnp_Params, { encode: false });
  const hmac = crypto.createHmac("sha512", vnpConfig.vnp_HashSecret);
  const signed = hmac.update(Buffer.from(signData, "utf-8")).digest("hex");

  vnp_Params["vnp_SecureHashType"] = "SHA512";
  vnp_Params["vnp_SecureHash"] = signed;

  const paymentUrl = `${vnpConfig.vnp_Url}?${qs.stringify(vnp_Params, {
    encode: false,
  })}`;
  // console.log("Payment URL:", paymentUrl);
  // console.log("amount =", amount);
  // console.log("orderInfo =", orderInfo);
  // console.log("bankCode =", bankCode);
  console.log("VNP PARAMS:", vnp_Params);
  return res.json({ code: "00", message: "success", data: paymentUrl });
};

// HÀM XỬ LÝ TRẢ VỀ TỪ VNPay
const handleVNPayReturn = async (req, res) => {
  const query = req.query;
  const vnp_HashSecret = vnpConfig.vnp_HashSecret;

  const vnp_SecureHash = query.vnp_SecureHash;
  delete query.vnp_SecureHash;
  delete query.vnp_SecureHashType;

  const sortedQuery = sortObject(query);
  const signData = qs.stringify(sortedQuery, { encode: false });
  const hmac = crypto.createHmac("sha512", vnp_HashSecret);
  const signed = hmac.update(Buffer.from(signData, "utf-8")).digest("hex");
  console.log("SIGN DATA:", signData);
  console.log("SIGNED HASH:", signed);
  console.log("RECEIVED HASH:", vnp_SecureHash);

  if (signed === vnp_SecureHash) {
    if (query.vnp_ResponseCode === "00") {
      const amount = query.vnp_Amount / 100;
      const orderInfo = query.vnp_OrderInfo;
      let maKH = null;

      if (orderInfo && orderInfo.includes("-")) {
        maKH = orderInfo.split("-")[1];
      } else {
        return res.status(400).send("orderInfo không hợp lệ");
      }

      try {
        const [donHangResult] = await connection.query(
          `INSERT INTO dathang (idKH, TongSL, Tongtien) VALUES (?, ?, ?)`,
          [maKH, 1, amount]
        );

        const maDonHang = donHangResult.insertId;
        const [cartItems] = await connection.query(
          `SELECT tenHang, donGia, soLuong FROM giohang WHERE maKH = ?`,
[maKH]
        );

        for (let item of cartItems) {
          await connection.query(
            `INSERT INTO ctdh (idDH, tenHang, donGia, soLuong) VALUES (?, ?, ?, ?)`,
            [maDonHang, item.tenHang, item.donGia, item.soLuong]
          );
        }

        await connection.query(`DELETE FROM giohang WHERE maKH = ?`, [maKH]);

        res.send("Thanh toán thành công!");
      } catch (error) {
        console.error("Lỗi khi lưu đơn hàng:", error);
        res.status(500).send("Lỗi xử lý đơn hàng sau thanh toán");
      }
    } else {
      res.send("Giao dịch không thành công");
    }
  } else {
    res.send("Sai chữ ký!");
  }
};

// HÀM SẮP XẾP OBJECT THEO THỨ TỰ KEY (QUAN TRỌNG)
function sortObject(obj) {
  const sorted = {};
  const keys = Object.keys(obj).sort();
  for (const key of keys) {
    sorted[key] = obj[key];
  }
  return sorted;
}

const handleVNPayIPN = async (req, res) => {
  const query = req.query;
  const vnp_HashSecret = vnpConfig.vnp_HashSecret;

  const vnp_SecureHash = query.vnp_SecureHash;
  delete query.vnp_SecureHash;
  delete query.vnp_SecureHashType;

  const sortedQuery = sortObject(query);
  const signData = qs.stringify(sortedQuery, { encode: false });
  const hmac = crypto.createHmac("sha512", vnp_HashSecret);
  const signed = hmac.update(Buffer.from(signData, "utf-8")).digest("hex");

  if (signed === vnp_SecureHash) {
    const responseCode = query.vnp_ResponseCode;
    const txnRef = query.vnp_TxnRef;
    const amount = query.vnp_Amount / 100;
    const maKH = query.vnp_OrderInfo?.split("-")[1];

    if (responseCode === "00") {
      // xử lý cập nhật đơn hàng ở đây giống return URL nếu cần
      res.status(200).json({ RspCode: "00", Message: "Thanh toán thành công" });
    } else {
      res.status(200).json({ RspCode: "01", Message: "Giao dịch thất bại" });
    }
  } else {
    res.status(200).json({ RspCode: "97", Message: "Sai chữ ký" });
  }
};
const createMoMoPayment = async (req, res) => {
  const { amount } = req.body;
  const { id } = req.params;
  const orderInfo = `ThanhToan-${id}`;
  const partnerCode = "MOMO";
  const accessKey = "F8BBA842ECF85";
  const secretKey = "K951B6PE1waDMi640xX08PD3vg6EkVlz";
  const redirectUrl = "http://192.168.1.129:3000/api/momo_return";
  const ipnUrl = "http://192.168.1.129:3000/api/momo_return";
  const requestType = "payWithMethod";
  const orderId = partnerCode + new Date().getTime();
  const requestId = orderId;
  const extraData = "";
  const autoCapture = true;
  const lang = "vi";

  // Raw signature
  const rawSignature = `accessKey=${accessKey}&amount=${amount}&extraData=${extraData}&ipnUrl=${ipnUrl}&orderId=${orderId}&orderInfo=${orderInfo}&partnerCode=${partnerCode}&redirectUrl=${redirectUrl}&requestId=${requestId}&requestType=${requestType}`;
  const signature = crypto.createHmac("sha256", secretKey).update(rawSignature).digest("hex");

  const requestBody = JSON.stringify({
    partnerCode,
    partnerName: "Test",
    storeId: "MomoTestStore",
    requestId,
    amount,
    orderId,
    orderInfo,
    redirectUrl,
    ipnUrl,
    lang,
    requestType,
    autoCapture,
    extraData,
    orderGroupId: "",
    signature
  });
  const options = {
    method: "POST",
    url: "https://test-payment.momo.vn/v2/gateway/api/create",
    headers: {
      "Content-Type": "application/json",
      "Content-Length": Buffer.byteLength(requestBody),
    },
    data: requestBody,
  };

  let result;
  try {
    const response = await axios(options);
    return res.status(200).json(response.data);
  } catch (error) {
    return res.status(500).json({
      status: 500,
      message: "Lỗi khi tạo thanh toán MoMo",
    }
    );
  }
};

const handleMoMoReturn = async (req, res) => {
  // Lấy thông tin giao dịch MoMo từ redirect
  const query = req.query;

  if (query && query.resultCode === '0') {
    // Giao dịch thành công
    const amount = parseInt(query.amount);
    const maKH = extractMaKH(query.orderInfo); // Tự viết hàm extractMaKH nếu cần
    function extractMaKH(orderInfo) {
  try {
    if (orderInfo && orderInfo.includes("-")) {
      return orderInfo.split("-")[1];
    }
  } catch (e) {}
  return null;
}

    try {
      const [donHangResult] = await connection.query(
        `INSERT INTO dathang (idKH, TongSL, Tongtien) VALUES (?, ?, ?)`,
        [maKH, 1, amount]
      );

      const maDonHang = donHangResult.insertId;
      const [cartItems] = await connection.query(
        `SELECT tenHang, donGia, soLuong FROM giohang WHERE maKH = ?`,
        [maKH]
      );

      for (let item of cartItems) {
        await connection.query(
          `INSERT INTO ctdh (idDH, tenHang, donGia, soLuong) VALUES (?, ?, ?, ?)`,
          [maDonHang, item.tenHang, item.donGia, item.soLuong]
        );
      }

      await connection.query(`DELETE FROM giohang WHERE maKH = ?`, [maKH]);

      return res.send("Thanh toán MoMo thành công!");
    } catch (error) {
      return res.status(500).send("Lỗi xử lý đơn hàng MoMo");
    }
  } else {
    return res.send("Thanh toán MoMo thất bại hoặc bị huỷ!");
  }
};
const clearCartApi = async (req, res) => {
  const maKH = req.params.maKH;
  try {
    await connection.query("DELETE FROM giohang WHERE maKH = ?", [maKH]);
    res.json({ success: true, message: "Đã xóa giỏ hàng thành công" });
  } catch (error) {
    console.error("Lỗi xóa giỏ hàng:", error);
    res.status(500).json({ error: "Lỗi khi xóa giỏ hàng" });
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
  thanhToanApi,
  createVNPayPayment,
  handleVNPayReturn,
  handleVNPayIPN,
  createMoMoPayment,
  handleMoMoReturn,
  clearCartApi,
};
