import { Component } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { AnimeShared } from "../shared/anime-details";
import { Storage } from "@ionic/storage";

@Component({
  selector: "app-tab1",
  templateUrl: "tab1.page.html",
  styleUrls: ["tab1.page.scss"],
})
export class Tab1Page {
  animeUrl = "https://rhinestone-bow-ketchup.glitch.me/api/v1/Search/";
  searchUrl = this.animeUrl + "/Search/";
  data;
  searchset;
  searched = false;
  popularAnimes;
  episodes;
  toggled = true;
  genres;
  onGoing;
  movies;
  genresofMovies;
  iconState;
  page_number = 1;
  storageArray = [];
  temp = [];
  title;
  loading = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private storage: Storage
  ) {}
  slideOpts = {
    slidesPerView: 3,
  };

  onClick(titulli, pershkrimi, imazhi, episodet) {
    this.router.navigateByUrl("/anime-details");
    AnimeShared.title = titulli;
    AnimeShared.description = pershkrimi;
    AnimeShared.img = imazhi;
    AnimeShared.episodes = episodet;
  }

  like(titulli, pershkrimi, imazhi) {
    this.title = titulli;
    var buton = document.getElementById(titulli);
    buton.setAttribute("name", "heart");
    this.iconState = buton.getAttribute("name");
    const storage = {
      titulli: titulli,
      pershkrimi: pershkrimi,
      img: imazhi,
      icoState: this.iconState + "-outline",
    };
    this.storage.get("bookmarks").then((data) => {
      this.temp = data;

      console.log(data.indexOf(storage));

      this.temp.push(storage);

      this.storage.set("bookmarks", this.temp);
    });
  }

  checkIfLiked(a) {}

  ngOnInit() {
    this.getPopular();
    this.getOngoingSeries();
    this.getMovies(false, "");
  }
  getMovies(isLoaded, event) {
    return this.http
      .get(
        "https://rhinestone-bow-ketchup.glitch.me/api/v1/NewSeasons/" +
          this.page_number
      )
      .subscribe((data) => {
        this.movies = data["anime"];
        this.loading = true;
        for (let i = 0; i < this.movies.length; i++) {
          this.genresofMovies = this.movies[i]["genres"];
        }
        if (isLoaded) event.target.complete();
        this.page_number++;
      });
  }
  doInfinite(event) {
    this.getMovies(true, event);
  }

  getPopular() {
    var randomNumber = Math.floor(Math.random() * 20).toString();
    return this.http
      .get(
        "https://rhinestone-bow-ketchup.glitch.me/api/v1/Popular/" +
          randomNumber
      )
      .subscribe((data) => {
        this.popularAnimes = data["popular"];
        console.log(this.popularAnimes);
      });
  }

  getOngoingSeries() {
    return this.http
      .get("https://rhinestone-bow-ketchup.glitch.me/api/v1/OngoingSeries")
      .subscribe((data) => {
        this.onGoing = data["anime"];
      });
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
          this.checkIfLiked(this.searchset[i]["title"]);
          this.genres = this.searchset[i]["genres"];
        }
      });
    }
  }
}
