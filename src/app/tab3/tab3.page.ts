import { Component } from "@angular/core";
import { Storage } from "@ionic/storage";
import { HttpClient } from "@angular/common/http";
@Component({
  selector: "app-tab3",
  templateUrl: "tab3.page.html",
  styleUrls: ["tab3.page.scss"],
})
export class Tab3Page {
  bookmarked = [];
  searched = false;
  searchset;
  genres;
  animeUrl = "https://salty-anchorage-64305.herokuapp.com/api/v1/Search/";
  constructor(private http: HttpClient, private storage: Storage) {}
  ionViewDidEnter() {
    this.storage.ready().then(() => {
      this.storage.get("bookmarks").then((data) => {
        this.bookmarked = data;
        console.log(this.bookmarked);
      });
    });
  }
  homelogo() {
    this.searched = false;
  }

  removeItem(i) {
    this.bookmarked.splice(i, 1);
    this.storage.set("bookmarks", this.bookmarked);
  }

  filterAnime(evt) {
    var q = evt.target.value;
    if (q.trim() == "") {
      this.searched = false;
    } else {
      this.searched = true;
      this.http.get(this.animeUrl + q.trim()).subscribe((data) => {
        this.searchset = data["search"];
        for (let i = 0; i < this.searchset.length; i++) {
          this.genres = this.searchset[i]["genres"];
        }
      });
    }
  }
}
