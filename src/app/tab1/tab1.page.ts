import { Component } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Router } from "@angular/router";
import { AnimeShared } from "../shared/anime-details";
import { Storage } from "@ionic/storage";
import { withCache } from "@ngneat/cashew";

@Component({
  selector: "app-tab1",
  templateUrl: "tab1.page.html",
  styleUrls: ["tab1.page.scss"],
})
export class Tab1Page {
  animeUrl = "https://salty-anchorage-64305.herokuapp.com/api/v1/Search/";
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
  connectedCard1 = false;
  connectedCard2 = false;

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

  homelogo() {
    this.searched = false;
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

      console.log(data);

      this.temp.push(storage);
      this.storage.set("bookmarks", this.temp);
    });
  }

  ngOnInit() {
    this.getPopular();
    this.getOngoingSeries();
    this.getMovies();
  }

  getMovies() {
    return this.http
      .get(
        "https://salty-anchorage-64305.herokuapp.com/api/v1/NewSeasons/" +
          this.page_number,
        withCache()
      )
      .subscribe((data) => {
        this.movies = data["anime"];
        console.log(data);
        for (let i = 0; i < this.movies.length; i++) {
          this.genresofMovies = this.movies[i]["genres"];
        }
      });
  }

  getPopular() {
    var randomNumber = Math.floor(Math.random() * 20).toString();
    return this.http
      .get(
        "https://salty-anchorage-64305.herokuapp.com/api/v1/Popular/" +
          randomNumber,
        withCache()
      )
      .subscribe((data) => {
        this.popularAnimes = data["popular"];
        this.connectedCard1 = true;
        console.log(this.popularAnimes);
      });
  }

  getOngoingSeries() {
    return this.http
      .get(
        "https://salty-anchorage-64305.herokuapp.com/api/v1/OngoingSeries",
        withCache()
      )
      .subscribe((data) => {
        this.onGoing = data["anime"];
        this.connectedCard2 = true;
      });
  }

  filterAnime(evt) {
    var q = evt.target.value;
    if (q.trim() == "") {
      this.searched = false;
    } else {
      this.searched = true;
      this.http.get(this.animeUrl + q.trim(), withCache()).subscribe((data) => {
        this.searchset = data["search"];
        for (let i = 0; i < this.searchset.length; i++) {
          this.genres = this.searchset[i]["genres"];
        }
      });
    }
  }
}
