<template>
  <div>
    <header class="public-header">
      <div class="container">
        <div class="d-flex justify-content-between align-items-center pt-2 pb-3 pl-3 pr-3">
          <h1><a href="/"><i class="fa fa-rss-square logo-icon"></i> Feedich</a></h1>
          <a href="https://twitter.com/share?ref_src=twsrc%5Etfw" class="ml-auto twitter-share-button" data-show-count="false">Tweet</a>
        </div>
      </div>
    </header>

    <router-view />

    <footer class="public-footer">
      <div class="container">
      <div class="row">
        <div class="col-4 offset-2">
          <span class="logo"><i class="fa fa-rss-square logo-icon mr-1"></i> Feedich</span>
          <small class="d-block mb-3 text-muted">&copy; {{ new Date().getFullYear() }} feedich.com</small>

          <ul class="list-inline social-icon">
            <li class="list-inline-item"><a href="#"><i class="fab fa-twitter"></i></a></li>
            <li class="list-inline-item"><a href="#"><i class="fab fa-slack"></i></a></li>
            <li class="list-inline-item"><a href="#"><i class="fab fa-medium"></i></a></li>
          </ul>
        </div>
        <div class="col-3">
          <h5>About</h5>
          <ul class="list-unstyled text-small mb-0">
            <li class="mb-1"><router-link class="text-muted" to="/help">ヘルプ</router-link></li>
            <li class="mb-1"><router-link class="text-muted" to="/contact">お問い合わせ</router-link></li>
            <li><a class="text-muted" href="#">開発者ブログ</a></li>
          </ul>
        </div>
        <div class="col-3">
          <h5>Policy</h5>
          <ul class="list-unstyled text-small">
            <li><router-link class="text-muted" to="/terms">利用規約</router-link></li>
            <li><router-link class="text-muted" to="/policy">プライバシーポリシー</router-link></li>
          </ul>
        </div>
      </div>
      </div>
    </footer>
  </div>
</template>

<style>
.text-small {
  font-size: 0.75rem;
}

.public-header {
  background-color: #2c3e50;
  color: white;
  padding: 10px 0;
}

.public-header h1 {
  font-family: 'Bree Serif', serif;
  font-size: 1.8rem;
  margin: 0;
}

.public-header h1 a {
  color: white;
}

.public-footer {
  background-color: #2c3e50;
  color: white;
  padding: 30px 0 35px 0;
}
.public-footer .logo {
  font-family: 'Bree Serif', serif;
}

.public-footer .social-icon {
  font-size: 1.5rem;
}

.public-footer .social-icon a {
  color: white;
}

</style>
