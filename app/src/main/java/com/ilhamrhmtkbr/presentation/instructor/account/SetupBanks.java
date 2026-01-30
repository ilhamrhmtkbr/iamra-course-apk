package com.ilhamrhmtkbr.presentation.instructor.account;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilhamrhmtkbr.R;
import com.ilhamrhmtkbr.presentation.utils.helper.RecyclerViewsUtil;
import com.ilhamrhmtkbr.presentation.utils.component.InputText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface SetupBanks {
    Map<String, String> dataBanks = new HashMap<String, String>() {{
        put("aceh", "PT. BANK ACEH");
        put("aceh_syar", "PT. BPD ISTIMEWA ACEH SYARIAH");
        put("agris", "PT. BANK AGRIS");
        put("agroniaga", "PT. BANK RAKYAT INDONESIA AGRONIAGA TBK.");
        put("allo", "PT. ALLO BANK INDONESIA TBK.");
        put("amar", "PT. BANK AMAR INDONESIA");
        put("andara", "PT. BANK ANDARA");
        put("anglomas", "PT. ANGLOMAS INTERNATIONAL BANK");
        put("antar_daerah", "PT. BANK ANTAR DAERAH");
        put("anz", "PT. BANK ANZ INDONESIA");
        put("artajasa", "PT. ARTAJASA");
        put("artha", "PT. BANK ARTHA GRAHA INTERNASIONAL TBK.");
        put("bali", "PT. BANK PEMBANGUNAN DAERAH BALI");
        put("bangkok", "BANGKOK BANK PUBLIC CO.LTD");
        put("banten", "PT. BANK BANTEN");
        put("barclays", "PT BANK BARCLAYS INDONESIA");
        put("bca", "PT. BANK CENTRAL ASIA TBK.");
        put("bcad", "PT. BANK DIGITAL BCA");
        put("bca_syar", "PT. BANK BCA SYARIAH");
        put("bengkulu", "PT. BPD BENGKULU");
        put("bisnis", "PT. BANK BISNIS INTERNASIONAL");
        put("bjb", "PT. BANK PEMBANGUNAN DAERAH JABAR DAN BANTEN");
        put("bjb_syar", "PT. BANK JABAR BANTEN SYARIAH");
        put("bni", "PT. BANK NEGARA INDONESIA (PERSERO)");
        put("bnp", "PT. BANK NUSANTARA PARAHYANGAN");
        put("bnp_paribas", "PT. BANK BNP PARIBAS INDONESIA");
        put("boa", "BANK OF AMERICA NA");
        put("bri", "PT. BANK RAKYAT INDONESIA (PERSERO)");
        put("bsi", "PT. BANK SYARIAH INDONESIA TBK.");
        put("btn", "PT. BANK TABUNGAN NEGARA (PERSERO)");
        put("btn_syar", "PT. BANK TABUNGAN NEGARA (PERSERO) UNIT USAHA SYARIAH");
        put("btpn", "PT. BANK BTPN");
        put("btpn_syar", "PT. BANK TABUNGAN PENSIUNAN NASIONAL SYARIAH");
        put("bukopin", "PT BANK KB BUKOPIN TBK.");
        put("bukopin_syar", "PT. BANK SYARIAH BUKOPIN");
        put("bumiputera", "PT. BANK BUMIPUTERA");
        put("bumi_artha", "PT. BANK BUMI ARTA");
        put("capital", "PT BANK CAPITAL INDONESIA");
        put("centratama", "PT. CENTRATAMA NASIONAL BANK");
        put("chase", "JP MORGAN CHASE BANK, N.A");
        put("china", "BANK OF CHINA");
        put("china_cons", "CHINA CONSTRUCTION");
        put("chinatrust", "PT. BANK CTBC INDONESIA");
        put("cimb", "PT. BANK CIMB NIAGA TBK.");
        put("cimb_syar", "PT. BANK CIMB NIAGA TBK. - UNIT USAHA SYARIAH");
        put("cimb_rekening_ponsel", "PT. BANK CIMB NIAGA TBK. - REKENING PONSEL");
        put("cimb_va", "PT. BANK CIMB NIAGA TBK. - VIRTUAL ACCOUNT");
        put("citibank", "CITIBANK, NA");
        put("commonwealth", "PT. BANK COMMONWEALTH");
        put("danamon", "PT. BANK DANAMON INDONESIA TBK.");
        put("danamon_syar", "PT. BANK DANAMON INDONESIA UNIT USAHA SYARIAH");
        put("dbs", "PT. BANK DBS INDONESIA");
        put("deutsche", "DEUTSCHE BANK AG.");
        put("dipo", "PT. BANK DIPO INTERNATIONAL");
        put("diy", "PT. BANK PEMBANGUNAN DAERAH DIY");
        put("diy_syar", "PT.BANK PEMBANGUNAN DAERAH DIY UNIT USAHA SYARIAH");
        put("dki", "PT. BANK DKI");
        put("dki_syar", "PT. BANK DKI UNIT USAHA SYARIAH");
        put("ekonomi", "PT. BANK EKONOMI RAHARJA");
        put("fama", "PT. BANK FAMA INTERNATIONAL");
        put("ganesha", "PT. BANK GANESHA");
        put("gopay", "GO-PAY");
        put("hana", "PT. BANK KEB HANA INDONESIA");
        put("hs_1906", "PT. BANK HS 1906");
        put("hsbc", "PT. BANK HSBC INDONESIA");
        put("icbc", "PT. BANK ICBC INDONESIA");
        put("ina_perdana", "PT. BANK INA PERDANA");
        put("index_selindo", "BANK INDEX SELINDO");
        put("india", "PT. BANK OF INDIA INDONESIA TBK.");
        put("jago", "PT. BANK JAGO TBK.");
        put("jago_syar", "PT. BANK JAGO UNIT USAHA SYARIAH");
        put("jambi", "PT.BANK PEMBANGUNAN DAERAH JAMBI");
        put("jasa_jakarta", "PT. BANK JASA JAKARTA");
        put("jateng", "PT. BANK PEMBANGUNAN DAERAH JAWA TENGAH");
        put("jateng_syar", "PT. BANK PEMBANGUNAN DAERAH JAWA TENGAH");
        put("jatim", "PT. BANK PEMBANGUNAN DAERAH JATIM");
        put("jatim_syar", "PT.BANK PEMBANGUNAN DAERAH JATIM - UNIT USAHA SYARIAH");
        put("jtrust", "PT. BANK JTRUST INDONESIA TBK.");
        put("kalbar", "PT.BANK PEMBANGUNAN DAERAH KALBAR");
        put("kalbar_syar", "PT.BANK PEMBANGUNAN DAERAH KALBAR UUS");
        put("kalsel", "PT. BANK PEMBANGUNAN DAERAH KALSEL");
        put("kalsel_syar", "PT. BANK PEMBANGUNAN DAERAH KALSEL - UNIT USAHA SYARIAH");
        put("kalteng", "PT. BPD KALIMANTAN TENGAH");
        put("kaltim", "PT.BANK PEMBANGUNAN DAERAH KALTIM");
        put("kaltim_syar", "PT.BANK PEMBANGUNAN DAERAH KALTIM - UNIT USAHA SYARIAH");
        put("lampung", "PT.BANK PEMBANGUNAN DAERAH LAMPUNG");
        put("maluku", "PT.BANK PEMBANGUNAN DAERAH MALUKU");
        put("mandiri", "PT. BANK MANDIRI (PERSERO) TBK.");
        put("mandiri_taspen", "PT. BANK MANDIRI TASPEN POS");
        put("maspion", "PT. BANK MASPION");
        put("mayapada", "PT. BANK MAYAPADA TBK.");
        put("maybank", "PT. BANK MAYBANK INDONESIA TBK.");
        put("maybank_syar", "PT. BANK MAYBANK SYARIAH INDONESIA");
        put("maybank_uus", "PT. BANK MAYBANK INDONESIA TBK. UNIT USAHA SYARIAH");
        put("mayora", "PT. BANK MAYORA");
        put("mega_syar", "PT. BANK MEGA SYARIAH");
        put("mega_tbk", "PT. BANK MEGA TBK.");
        put("mestika", "PT. BANK MESTIKA DHARMA");
        put("metro", "PT. BANK METRO EXPRESS");
        put("mitraniaga", "PT. BANK MITRANIAGA");
        put("mitsubishi", "THE BANK OF TOKYO MITSUBISHI UFJ LTD.");
        put("mizuho", "PT. BANK MIZUHO INDONESIA");
        put("mnc", "PT. BANK MNC INTERNASIONAL TBK.");
        put("muamalat", "PT. BANK MUAMALAT INDONESIA");
        put("multiarta", "PT. BANK MULTI ARTA SENTOSA");
        put("mutiara", "PT. BANK MUTIARA TBK.");
        put("niaga_syar", "PT. BANK NIAGA TBK. SYARIAH");
        put("nagari", "PT. BANK NAGARI");
        put("nobu", "PT. BANK NATIONALNOBU");
        put("ntb", "PT. BANK PEMBANGUNAN DAERAH NTB");
        put("ntt", "PT.BANK PEMBANGUNAN DAERAH NTT");
        put("ocbc", "PT. BANK OCBC NISP TBK.");
        put("ocbc_syar", "PT. BANK OCBC NISP TBK. - UNIT USAHA SYARIAH");
        put("ok", "PT. BANK OKE INDONESIA");
        put("ovo", "OVO");
        put("panin", "PT. PANIN BANK TBK.");
        put("panin_syar", "PT. BANK PANIN SYARIAH");
        put("papua", "PT.BANK PEMBANGUNAN DAERAH PAPUA");
        put("permata", "PT. BANK PERMATA TBK.");
        put("permata_syar", "PT. BANK PERMATA TBK. UNIT USAHA SYARIAH");
        put("permata_va", "PT. BANK PERMATA TBK. - VIRTUAL ACCOUNT");
        put("prima_master", "PT. PRIMA MASTER BANK");
        put("pundi", "PT. BANK PUNDI INDONESIA");
        put("purba", "PT. BANK PURBA DANARTA");
        put("qnb", "PT. BANK QNB INDONESIA TBK.");
        put("rabobank", "PT. BANK RABOBANK INTERNATIONAL INDONESIA");
        put("rbos", "THE ROYAL BANK OF SCOTLAND N.V.");
        put("resona", "PT. BANK RESONA PERDANIA");
        put("riau", "PT. BANK PEMBANGUNAN DAERAH RIAU KEPRI");
        put("riau_syar", "PT. BANK PEMBANGUNAN DAERAH RIAU KEPRI SYARIAH");
        put("sampoerna", "PT. BANK SAHABAT SAMPOERNA");
        put("sbi", "PT. BANK SBI INDONESIA");
        put("seabank", "PT. BANK SEABANK INDONESIA");
        put("shinhan", "PT. BANK SHINHAN INDONESIA");
        put("sinarmas", "PT. BANK SINARMAS");
        put("sinarmas_syar", "PT. BANK SINARMAS UNIT USAHA SYARIAH");
        put("stanchard", "STANDARD CHARTERED BANK");
        put("sulselbar", "PT. BANK SULSELBAR");
        put("sulselbar_syar", "PT. BANK SULSELBAR UNIT USAHA SYARIAH");
        put("sulteng", "PT. BPD SULAWESI TENGAH");
        put("sultenggara", "PT. BPD SULAWESI TENGGARA");
        put("sulut", "PT. BANK SULUTGO");
        put("sumbar", "BPD SUMATERA BARAT");
        put("sumitomo", "PT. BANK SUMITOMO MITSUI INDONESIA");
        put("sumsel_babel", "PT. BPD SUMSEL DAN BABEL");
        put("sumsel_babel_syar", "PT. BPD SUMSEL DAN BABEL UNIT USAHA SYARIAH");
        put("sumut", "PT. BANK PEMBANGUNAN DAERAH SUMUT");
        put("sumut_syar", "PT. BANK PEMBANGUNAN DAERAH SUMUT UUS");
        put("uob", "PT. BANK UOB INDONESIA");
        put("victoria", "PT. BANK VICTORIA INTERNATIONAL");
        put("victoria_syar", "PT. BANK VICTORIA SYARIAH");
        put("woori", "PT. BANK WOORI SAUDARA INDONESIA 1906 TBK.");
        put("yudha_bhakti", "PT. BANK YUDHA BHAKTI");
    }};

    default void setupInputBanks(
            TextView inputBank,
            LayoutInflater inflater,
            Context context) {

        inputBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView = inflater.inflate(R.layout.dialog_custom_instructor_account_bank_choise, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);

                InputText searchBank = dialogView.findViewById(R.id.input_bank);
                RecyclerView banks = dialogView.findViewById(R.id.banks);

                List<AccountBanksAdapter.BankItem> bankList = new ArrayList<>();
                for (Map.Entry<String, String> entry : dataBanks.entrySet()) {
                    bankList.add(new AccountBanksAdapter.BankItem(entry.getKey(), entry.getValue()));
                }

                AccountBanksAdapter adapter = new AccountBanksAdapter(bankList, new AccountBanksAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(AccountBanksAdapter.BankItem item) {
                        inputBank.setText(item.key);
                        dialog.dismiss();
                    }
                });

                banks.addItemDecoration(new RecyclerViewsUtil.VerticalSpaceItemBorder(context));
                banks.setLayoutManager(new LinearLayoutManager(context));
                banks.setAdapter(adapter);

                searchBank.getInput().addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String keyword = s.toString().toLowerCase();
                        List<AccountBanksAdapter.BankItem> filtered = new ArrayList<>();
                        for (AccountBanksAdapter.BankItem item : bankList) {
                            if (item.key.toLowerCase().contains(keyword) || item.name.toLowerCase().contains(keyword)) {
                                filtered.add(item);
                            }
                        }
                        adapter.updateList(filtered);
                    }
                });

                dialog.show();
            }
        });
    }

}
