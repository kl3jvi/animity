import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { IonicModule } from "@ionic/angular";

import { AnimeDetailsPageRoutingModule } from "./anime-details-routing.module";

import { AnimeDetailsPage } from "./anime-details.page";

import { VideoplayerPage } from "../modals/videoplayer/videoplayer.page";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    AnimeDetailsPageRoutingModule,
  ],
  declarations: [AnimeDetailsPage, VideoplayerPage],
  entryComponents: [VideoplayerPage],
})
export class AnimeDetailsPageModule {}
