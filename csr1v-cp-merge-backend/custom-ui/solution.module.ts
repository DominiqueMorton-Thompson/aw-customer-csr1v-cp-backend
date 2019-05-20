import { NgModule, Injector } from '@angular/core';
import { VulcanuxCoreModule, WebixComponentsRegistry } from '@apporchid/vulcanuxcore';
import { VulcanuxControlsModule } from '@apporchid/vulcanuxcontrols';
import { HttpModule } from '@angular/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import './components/custom-protoui.component';

const entryComponents = [
    
];
@NgModule({
    declarations: [],
    imports: [
        VulcanuxCoreModule,
        VulcanuxControlsModule,
        HttpModule,
        CommonModule,
        FormsModule,
        HttpClientModule,
    ],
    entryComponents: [],
})
export class SolutionModule {
    constructor(private injector: Injector) {        
    }
}