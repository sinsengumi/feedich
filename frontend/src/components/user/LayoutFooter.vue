<template>
  <footer class="footer fixed-bottom d-flex flex-row justify-content-between align-items-center">
    <ul class="footer-navi d-flex flex-row">
      <li><router-link to="/terms">サービス利用規約</router-link></li>
      <li><router-link to="/policy">プライバシーポリシー</router-link></li>
      <li><router-link to="/contact">お問い合わせ</router-link></li>
      <li><a href="#">開発者ブログ</a></li>
    </ul>
    <div class="copyright">
      <span>&copy; {{ new Date().getFullYear() }} feedich.com</span>
    </div>
  </footer>
</template>

<script>
</script>

<style scoped>
</style>
