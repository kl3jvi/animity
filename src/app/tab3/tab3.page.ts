import { Component } from "@angular/core";
import { Storage } from "@ionic/storage";
@Component({
  selector: "app-tab3",
  templateUrl: "tab3.page.html",
  styleUrls: ["tab3.page.scss"],
})
export class Tab3Page {
  bookmarked = [];

  constructor(private storage: Storage) {}
  ionViewDidEnter() {
    this.storage.ready().then(() => {
      this.storage.get("bookmarks").then((data) => {
        this.bookmarked = data;
        console.log(this.bookmarked);
      });
    });
  }

  removeItem(i) {
    this.bookmarked.splice(i, 1);
    this.storage.set("bookmarks", this.bookmarked);
  }
}
