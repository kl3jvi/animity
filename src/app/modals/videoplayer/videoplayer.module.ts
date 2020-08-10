import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from "@angular/forms";

import { IonicModule } from "@ionic/angular";

import { VideoplayerPageRoutingModule } from "./videoplayer-routing.module";

import { VideoplayerPage } from "./videoplayer.page";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    IonicModule,
    VideoplayerPageRoutingModule,
  ],
  declarations: [],
})
export class VideoplayerPageModule {}
