function adminLogin(){
    document.getElementById('login-form').addEventListener('submit', async (e) => {
        e.preventDefault();

        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        try {
            const response = await axios.post('/api/login', {
                email: email,
                password: password
            });

            const accessToken = response.headers['access'];
            localStorage.setItem('access', accessToken);

            console.log('Access Token 저장 완료:', accessToken);

            window.location.href = '/admin/console';
        } catch (error) {
            console.error('Error:', error);
            alert('로그인 실패');
        }
    });
}