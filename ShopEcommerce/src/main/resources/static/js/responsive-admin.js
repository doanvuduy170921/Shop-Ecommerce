// Chức năng toggle sidebar
document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.getElementById('menuToggle');
    const sidebar = document.querySelector('.sidebar');
    const header = document.querySelector('header');

    // Lấy chiều cao của header để đặt vị trí sidebar và nút menu
    const headerHeight = header ? header.offsetHeight : 60;

    // Điều chỉnh vị trí của nút menu và sidebar dựa trên chiều cao header
    if (menuToggle) {
        menuToggle.style.top = (headerHeight + 10) + 'px';
    }


    // Thêm sự kiện click cho nút menu
    if (menuToggle) {
        menuToggle.addEventListener('click', function(e) {
            e.stopPropagation(); // Ngăn sự kiện click lan ra document
            sidebar.classList.toggle('active');
        });
    }

    // Đóng sidebar khi click bên ngoài
    document.addEventListener('click', function(event) {
        if (sidebar && !sidebar.contains(event.target) &&
            menuToggle && !menuToggle.contains(event.target) &&
            sidebar.classList.contains('active')) {
            sidebar.classList.remove('active');
        }
    });
});