<template>
  <div class="setting">
    <div class="card">
    <h5 class="card-header">設定</h5>
  <div class="card-body">

    <div class="row">
  <div class="col-3">
    <div class="nav flex-column nav-pills" id="v-pills-tab" role="tablist" aria-orientation="vertical">
      <a class="nav-link active" id="v-pills-home-tab" data-toggle="pill" href="#v-pills-home" role="tab" aria-controls="v-pills-home" aria-selected="true">表示の設定</a>
      <a class="nav-link" id="v-pills-profile-tab" data-toggle="pill" href="#v-pills-profile" role="tab" aria-controls="v-pills-profile" aria-selected="false">スターの設定</a>
      <a class="nav-link" id="v-pills-messages-tab" data-toggle="pill" href="#v-pills-messages" role="tab" aria-controls="v-pills-messages" aria-selected="false">インポート・エクスポート</a>
    </div>
  </div>
  <div class="col-9">
    <div class="tab-content" id="v-pills-tabContent">
      <div class="tab-pane fade show active" id="v-pills-home" role="tabpanel" aria-labelledby="v-pills-home-tab">あああああああああ</div>
      <div class="tab-pane fade" id="v-pills-profile" role="tabpanel" aria-labelledby="v-pills-profile-tab">いいいいいいいいい</div>
      <div class="tab-pane fade" id="v-pills-messages" role="tabpanel" aria-labelledby="v-pills-messages-tab">うううううううううう</div>
    </div>
  </div>
</div>

  </div>
</div>

<b-card no-body>
  <b-tabs pills card vertical>
    <b-tab title="表示の設定" active>
      Tab Contents 1
    </b-tab>
    <b-tab title="スターの設定">
      Tab Contents 2
    </b-tab>
    <b-tab title="インポート・エクスポート">
      Tab Contents 3
    </b-tab>
  </b-tabs>
</b-card>
</div>
</template>

<script>
export default {
}
</script>

<style scoped>
</style>
