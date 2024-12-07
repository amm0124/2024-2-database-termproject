function adminSignUp(){
    document.getElementById('login-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        try {
            const response = await axios.post('/api/v1/admin/register/admin', {
                email: email,
                password: password
            });

            alert('관리자 회원가입 성공');

            window.location.href = '/admin/login';
        } catch (error) {
            console.error('Error:', error);
            alert(' 관리자 회원가입 실패');
        }
    });
}